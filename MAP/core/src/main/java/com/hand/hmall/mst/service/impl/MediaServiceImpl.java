package com.hand.hmall.mst.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.hand.common.util.Auth;
import com.hand.common.util.SecretKey;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.mybatis.entity.Example;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hmall.log.service.ILogManagerService;
import com.hand.hmall.mst.dto.Catalogversion;
import com.hand.hmall.mst.dto.Media;
import com.hand.hmall.mst.dto.MediaDto;
import com.hand.hmall.mst.dto.Product;
import com.hand.hmall.mst.mapper.MediaMapper;
import com.hand.hmall.mst.service.ICatalogversionService;
import com.hand.hmall.mst.service.IMediaService;
import com.hand.hmall.mst.service.IProductService;
import com.hand.hmall.util.Constants;
import com.markor.map.framework.restclient.RestClient;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiaoli.yu
 * @version 0.1
 * @name:
 * @Description: 多媒体的service实现类
 * @date 2017/7/10 14:37
 */
@Service
@Transactional
public class MediaServiceImpl extends BaseServiceImpl<Media> implements IMediaService {

    private static final Logger logger = LoggerFactory.getLogger(MediaServiceImpl.class);
    private static final String JOB_DESCRIPTION = "商品图片同步ZMALL";
    private static final String GROUP_NAME_SPLIT = "_";
    private static final String GROUP_PROP_SUFFIX = "List";
    private static final String URL = "/zmallsync/imageSync";

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private ICatalogversionService iCatalogversionService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ILogManagerService iLogManagerService;

    @Autowired
    private RestClient restClient;

    @Value("#{configProperties['nfsImageRootPath']}")
    private String nfsImageRootPath;

    @Resource(name = "imagePathNamesProperties")
    private Properties imagePathNamesProperties;

    /**
     * 推送sync为N的多媒体信息到商城当中
     *
     * @return
     */
    @Override
    public List<MediaDto> selectPushingMedia() {
        // TODO Auto-generated method stub
        return mediaMapper.selectPushingMedia();
    }

    /**
     * 更新多媒体接口推送标志
     *
     * @param dto
     */
    @Override
    public void updateMediaSyncflag(List<MediaDto> dto) {
        mediaMapper.updateMediaSyncflag(dto);
    }

    /**
     * 对应目录版本多媒体信息查询
     *
     * @param media
     * @return
     */
    @Override
    public Long selectByCodeAndVersion(Media media) {
        return mediaMapper.selectByCodeAndVersion(media);
    }


    /**
     * 为选中商品生成或更新媒体对象
     *
     * @param products 商品集合，必须包含code属性
     */
    @Override
    public void createMediaForProduct(List<Product> products) {
        // 完整的图片路径类似，/home/images/products/Z31CK01/SM_IMAGE/Z31CK01_SM_004.jpg

        for (Product product : products) {
            Assert.notNull(product.getProductId(), "商品id不能为空");
            Assert.notNull(product.getCode(), "商品编码不能为空");
        }

        // 读取配置文件
        Map<String, String> imagePathNames = getImagePathNames();

        // 判断图片根路径是否存在
        File homeImageFile = new File(nfsImageRootPath);
        if (!homeImageFile.exists()) {
            throw new RuntimeException("图片根路径" + nfsImageRootPath + "不存在");
        }

        // 查询stage的目录版本
        Catalogversion catalogversion = new Catalogversion();
        catalogversion.setCatalogName(Constants.CATALOG_VERSION_MARKOR);
        catalogversion.setCatalogversion(Constants.CATALOG_VERSION_STAGED);
        Long catalogversionId = iCatalogversionService.selectCatalogversionId(catalogversion);

        for (Product product : products) {

            logger.info("Product {} begin fetch images.", product.getCode());

            // 判断商品路径是否存在，如/home/images/products/Z31CK01
            String productImagePath = nfsImageRootPath + product.getCode();
            File productImageFile = new File(productImagePath);
            if (!productImageFile.exists()) {
                throw new RuntimeException("商品[" + product.getCode() + "]的图片路径不存在");
            }

            // 查询该商品在媒体表中的所有媒体对象，用于和nfs中的图片做比较，来判断nfs的操作是新增、更新或删除
            List<Media> mediaList = selectAllByProductId(product.getProductId(), catalogversionId);

            // 由于人工导入的原因可能存在有些媒体的图片组为空，忽略这些数据并分组
            Map<String, List<Media>> mediaGroupMap = mediaList.stream()
                    .filter(media -> media.getImageGroup() != null)
                    .collect(Collectors.groupingBy(Media::getImageGroup));

            // 按照预设图片组进行目录的遍历
            for (String imagePathNameStr : imagePathNames.keySet()) {

                // 获取图片分组的路径，/home/images/products/Z31CK01/SM_IMAGE
                String imageGroupPath = productImagePath + File.separator + imagePathNameStr;
                File imageGroupFile = new File(imageGroupPath);
                List<Media> mediaGroup = mediaGroupMap.get(imagePathNameStr);
                if (!imageGroupFile.exists()) {
                    if (mediaGroup != null) {
                        // 如果nfs图片组不存在，而数据库中图片组存在，表明该nfs图片组已经被删除，打删除标记
                        for (Media media : mediaGroup) {
                            media.setUpdateFlag(Constants.UPDATE_FLAG_DEL);
                            this.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), media);
                        }
                    }
                } else {

                    // 如果nfs中该图片组存在，则筛选图片组中的合法图片，类似于Z31CK01_SM_004.jpg这种格式
                    File[] imageFiles = imageGroupFile.listFiles(filename -> {
                        Pattern pattern = Pattern.compile("^" + product.getCode() + "_" + imagePathNameStr.split("_")[0] + "_[0-9]+\\.(jpg|png)$");
                        return pattern.matcher(filename.getName()).matches();
                    });

                    // 遍历nfs中该图片组下所有图片，并跟数据库中的图片组做对比，判断其中多媒体是新增、修改、删除
                    for (File file : imageFiles) {

                        // 获取文件全名称，如Z00DT02_SM_001.jpg
                        String fullName = file.getName();

                        //去除后缀名，获取文件名，如Z00DT02_SM_001
                        String simpleName = fullName.split("\\.")[0];
                        String sort = simpleName.split("_")[2];

                        // 查看该图片文件是否存在于多媒体中
                        Media imageMedia = null;
                        if (mediaGroup != null) {
                            imageMedia = mediaGroup.stream()
                                    .filter(media -> media.getCode().equals(simpleName))
                                    .findFirst().orElse(null);
                        }

                        // 对文件内容进行加密，如果文件修改时间无法判断是否是新图片则根据内容判断
                        String fileEncoding = encodeImageFile(file);

                        // 图片相对路径
                        String relativePath = file.getPath().replace(nfsImageRootPath, File.separator);

                        if (imageMedia == null) {
                            // 数据库中不存在对应的媒体对象，表示该图片为新图片，保存一个新的媒体对象
                            imageMedia = new Media();
                            imageMedia.setCatalogversionId(catalogversionId);
                            imageMedia.setProductId(product.getProductId());
                            imageMedia.setImageGroup(imagePathNameStr);
                            imageMedia.setCode(simpleName);
                            imageMedia.setName(file.getName());
                            imageMedia.setPath(relativePath);
                            imageMedia.setMd5Content(fileEncoding);
                            imageMedia.setUrl("");
                            imageMedia.setImageLastUpdate(new Date(file.lastModified()));
                            imageMedia.setSort(sort);
                            // 新图片的图片更新标志位'C'
                            imageMedia.setUpdateFlag(Constants.UPDATE_FLAG_CREATE);
                            logger.info("imageMedia is: {}", new Gson().toJson(imageMedia));
                            this.insertSelective(RequestHelper.newEmptyRequest(), imageMedia);
                        } else {

                            // 如果数据中存在于nfs对应的媒体对象，则判断该图片是否发生过变更，判断依据如下
                            if (Constants.UPDATE_FLAG_DEL.equals(imageMedia.getUpdateFlag())
                                    || StringUtils.isBlank(imageMedia.getSort()) || StringUtils.isBlank(imageMedia.getPath())
                                    || (imageMedia.getImageLastUpdate() != null && new Date(file.lastModified()).after(imageMedia.getImageLastUpdate()))
                                    || !fileEncoding.equals(imageMedia.getMd5Content())) {
                                if (Constants.UPDATE_FLAG_DEL.equals(imageMedia.getUpdateFlag())) {
                                    imageMedia.setUpdateFlag(Constants.UPDATE_FLAG_CREATE);
                                } else {
                                    imageMedia.setUpdateFlag(Constants.UPDATE_FLAG_UPDATE);
                                }
                                if (StringUtils.isBlank(imageMedia.getName())) {
                                    imageMedia.setName(file.getName());
                                }
                                imageMedia.setPath(relativePath);
                                imageMedia.setSort(sort);
                                imageMedia.setImageLastUpdate(new Date(file.lastModified()));
                                imageMedia.setMd5Content(fileEncoding);
                                imageMedia.setIsHandle("Y");
                                this.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), imageMedia);
                            }
                        }
                    }

                    // 对于相同图片组中nfs中不存在，但是媒体表中存在的媒体对象打删除标记
                    List<String> simpleNameList = Stream.of(imageFiles).map(file -> file.getName().split("\\.")[0]).collect(Collectors.toList());
                    if (mediaGroup != null) {
                        mediaGroup.stream().filter(media -> !simpleNameList.contains(media.getCode()))
                                .forEach(media -> {
                                    media.setUpdateFlag(Constants.UPDATE_FLAG_DEL);
                                    this.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), media);
                                });
                    }
                }
            }

            // 修改商品抓取图片标识，控制图片抓取的平率
            product.setFetchImageFlag(Constants.YES);
            productService.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), product);
        }
    }


    /**
     * 对图片文件进行md5和base64编码
     *
     * @param file 图片文件
     * @return String
     */
    private String encodeImageFile(File file) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            return base64en.encode(md5.digest(FileUtils.readFileToByteArray(file)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5编码获取失败");
        } catch (IOException e) {
            throw new RuntimeException("文件读取失败");
        }
    }

    /**
     * 读取图片组配置文件
     *
     * @return Map<String, String>
     */
    private Map<String, String> getImagePathNames() {
        Map<String, String> imagePathNames = new HashMap<>();
        imagePathNamesProperties.stringPropertyNames().stream()
                .forEach(pathname -> imagePathNames.put(pathname, imagePathNamesProperties.getProperty(pathname)));
        return imagePathNames;
    }

    /**
     * 根据商品id查询所有markorStaged版本的媒体对象
     *
     * @param productId        商品id
     * @param catalogversionId 目录版本id
     * @return
     */
    @Override
    public List<Media> selectAllByProductId(Long productId, Long catalogversionId) {
        Media media = new Media();
        media.setCatalogversionId(catalogversionId);
        media.setProductId(productId);
        return mapper.select(media);
    }

    /**
     * 查询发生过变动的图片组
     *
     * @param catalogversionId 目录版本
     * @param productId
     * @return
     */
    @Override
    public List<String> selectUpdatedImageGroup(Long catalogversionId, Long productId) {
        return mediaMapper.selectUpdatedImageGroup(catalogversionId, productId);
    }

    /**
     * 查询将要被推送的媒体对象
     *
     * @param catalogversionId 目录版本
     * @param productId        商品id
     * @param imageGroup       图片组
     * @return
     */
    @Override
    public List<Media> selectToBePushedMedias(Long catalogversionId, Long productId, String imageGroup) {
        return mediaMapper.selectToBePushedMedias(catalogversionId, productId, imageGroup);
    }

    /**
     * 查询一个商品的所有表删除Flag的媒体对象
     *
     * @param catalogversionId 目录版本
     * @param productId        商品id
     * @return
     */
    @Override
    public List<Media> selectToBeDeletedMedias(Long catalogversionId, Long productId) {
        Media media = new Media();
        media.setUpdateFlag(Constants.UPDATE_FLAG_DEL);
        media.setCatalogversionId(catalogversionId);
        media.setProductId(productId);
        return mediaMapper.select(media);
    }

    /**
     * 根据条件查询
     *
     * @param media 条件
     * @return
     */
    @Override
    public List<Media> select(Media media) {
        return mediaMapper.select(media);
    }

    /**
     * 根据example对象查询
     *
     * @param example 查询条件
     * @return
     */
    @Override
    public List<Media> selectByExample(Example example) {
        return mediaMapper.selectByExample(example);
    }

    /**
     * 图片导入时，校验图片数据逻辑
     *
     * @param request
     * @param list
     * @return
     */
    @Override
    public String checkMedia(IRequest request, List<Media> list) {

        String result = "";
        for (Media media : list) {

            if (media.getProductCode() == null || media.getCatalog() == null || media.getCode() == null || media.getImageGroup() == null) {
                result = result + "图片编码，商品编码,图片类型和目录版本不能为空!";
                continue;
            }

            //图片文案导入时，校验【图片编码】是否包含【商品编码】，如果不包含，则导入报错，提示【请检查文案是否导入到正确图片中】，同时数据不存入数据库
            if (media.getCode().indexOf(media.getProductCode()) == -1) {
                result = result + "请检查您输入的" + media.getCatalog() + "版本的商品编码" + media.getProductCode() + "文案是否导入到正确图片中!";
                continue;
            }

            int flag = 1;
            for (Catalogversion catalogversion : iCatalogversionService.selectCatalogVersion()) {
                if (media.getCatalog().equals(catalogversion.getCatalogName())) {
                    media.setCatalogversionId(catalogversion.getCatalogversionId());
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) {
                result = "您输入的目录版本不存在!";
                continue;
            }

            //校验图片类型以及sort
            String[] split = media.getCode().trim().split("_");
            if (split.length == 3) {
                if (media.getImageGroup() == null) {
                    media.setImageGroup(split[1] + "_IMAGE");
                }
                media.setSort(split[2]);
            }


            //验证商品
            Product p = new Product();
            p.setCode(media.getProductCode());
            p.setCatalogversionId(media.getCatalogversionId());
            List<Product> pList = productService.selectByCodeAndCatalogVersion(p);
            if (CollectionUtils.isEmpty(pList)) {
                result = "您输入的" + media.getCatalog() + "版本的商品编码" + media.getProductCode() + "不存在!";
                continue;
            } else {
                media.setProductId(pList.get(0).getProductId());
            }

            //验证图片，存在则更新，不存在则插入
            Media m = new Media();
            m.setCode(media.getCode());
            m.setCatalogversionId(media.getCatalogversionId());
            List<Media> mediaList = this.select(m);
            if (CollectionUtils.isEmpty(mediaList)) {
                //为空则不存在，即插入
                this.insertSelective(request, media);
            } else {
                //不为空则存在，即更新
                media.setMediaId(mediaList.get(0).getMediaId());
                this.updateByPrimaryKeySelective(request, media);
            }
        }
        return result;
    }

    /**
     * 商品详情页面中，查询商品相关的各种多媒体图片
     *
     * @param request
     * @param dto
     * @param pageSize
     * @param page
     * @return
     */
    @Override
    public List<Media> selectMediaByProduct(IRequest request, Media dto, int pageSize, int page) {
        PageHelper.startPage(page, pageSize);
        return mediaMapper.selectMediaByProduct(dto);
    }

    /**
     * 商品详情页面中删除图片关联关系
     *
     * @param request
     * @param list
     * @return
     */
    @Override
    public List<Media> deleteRelationWithProduct(IRequest request, List<Media> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (Media media : list) {
                mediaMapper.deleteRelationWithProduct(media);
            }
        }
        return list;
    }

    /**
     * 获取图片的IO流，以便页面显示图片
     *
     * @param request
     * @param filePath
     * @return
     * @throws IOException
     */
    @Override
    public String getImageIoStream(IRequest request, String filePath) throws IOException {
        byte[] buffer = null;
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
        byte[] b = new byte[1000];
        int n;
        while ((n = fis.read(b)) != -1) {
            bos.write(b, 0, n);
        }
        fis.close();
        bos.close();
        buffer = bos.toByteArray();
        String img = StringUtils.deleteWhitespace(Base64.encodeBase64String(buffer));
        return String.format("data:image/png;base64,%s", img);
    }

    /**
     * 根据商品ID获取对应的多媒体信息
     *
     * @param productId 商品ID
     * @return
     */
    @Override
    public List<Media> selectMediaByProductId(Long productId) {
        return mediaMapper.selectMediaByProductId(productId);
    }

    /**
     * 根据多媒体Id调用系统方法查询多媒体信息
     *
     * @param iRequest 请求体
     * @param mediaId  多媒体Id
     * @return
     */
    @Override
    public Media selectByMediaId(IRequest iRequest, Long mediaId) {
        Media media = new Media();
        media.setMediaId(mediaId);
        return this.selectByPrimaryKey(iRequest, media);
    }

    /**
     * 同步多媒体到zmall
     */
    @Override
    public void syncToZmall() {
        // 查询online目录版本
        Catalogversion catalogversion = new Catalogversion();
        catalogversion.setCatalogName(Constants.CATALOG_VERSION_MARKOR);
        catalogversion.setCatalogversion(Constants.CATALOG_VERSION_ONLINE);
        Long catalogversionId = iCatalogversionService.selectCatalogversionId(catalogversion);

        // 查询有哪些商品是发生图片变更的
        List<Product> productList = productService.selectProductWithMediaUpdated(catalogversionId, Integer.MAX_VALUE);
        iLogManagerService.logTrace(this.getClass(), JOB_DESCRIPTION, null, "发现" + CollectionUtils.size(productList) + "个待推送商品");
        if (CollectionUtils.isEmpty(productList)) {
            return;
        }

        // 记录推送成功的商品数量
        int count = 0;

        for (Product product : productList) {
            List<Media> allMedias = new ArrayList<>();
            JSONObject requestObj = new JSONObject();
            requestObj.put("code", product.getCode());

            // 查询该商品发生图片变更的组名称
            List<String> imageGroups = this.selectUpdatedImageGroup(catalogversionId, product.getProductId());

            // 如果组名称为空，认为是垃圾数据不进行推送
            imageGroups = imageGroups.stream().filter(groupName -> groupName != null).collect(Collectors.toList());

            // 循环图片组，每个图片组组装成一个媒体集合
            for (String imageGroup : imageGroups) {

                // 查询该商品该图片组下所有更新标志不为‘D’的媒体对象
                List<Media> mediaList = this.selectToBePushedMedias(catalogversionId, product.getProductId(), imageGroup);

                // updated by majun@2017/10/17 过滤掉sort和path属性为空的记录，这些可能是由于人工导入造成的垃圾数据
                mediaList = mediaList.stream().filter(media -> media.getSort() != null && media.getPath() != null).collect(Collectors.toList());

                allMedias.addAll(mediaList);

                // 组装json参数
                // json组装规则：若该图片组没有图片更新，则该组不推送；
                // 若有则推送，认为是全量更新，若为空集合，则认为该组图片全部删除
                JSONArray mediaArray = new JSONArray();
                for (Media media : mediaList) {
                    JSONObject mediaObj = new JSONObject();
                    mediaObj.put("code", media.getCode());
                    mediaObj.put("name", media.getName());
                    mediaObj.put("imageDescribe", media.getImageDescribe());
                    mediaObj.put("url", media.getPath());
                    mediaObj.put("sort", media.getSort());
                    mediaArray.add(mediaObj);
                }
                requestObj.put(transferGroupName(imageGroup), mediaArray);
            }

            String content = requestObj.toString();
            String token = Auth.md5(SecretKey.KEY + content);
            Map<String, String> parameters = new HashMap<>();
            parameters.put("token", token);

            try {
                Response response = restClient.postString(Constants.ZMALL, URL, content, Constants.MINI_TYPE_JSON, parameters, null);
                if (response.code() == 200) {

                    JSONObject responseObj = RestClient.responseToJSON(response);
                    if (Constants.JOB_STATUS_SUCCESS.equals(responseObj.getString("code"))) {

                        // 累加成功推送的商品数量
                        count++;

                        // 获取online版本中该商品所有待删除（‘D’）的媒体对象并删除
                        List<Media> toBeDeletedMedias = this.selectToBeDeletedMedias(catalogversionId, product.getProductId());
                        if (CollectionUtils.isNotEmpty(toBeDeletedMedias)) {
                            toBeDeletedMedias.stream().forEach(media -> this.deleteByPrimaryKey(media));
                        }

                        // 将所有已经推送成功后的媒体对象的同步标志更新为‘Y’
                        allMedias.stream().forEach(media -> {
                            media.setSyncflag(Constants.YES);
                            this.updateByPrimaryKeySelective(RequestHelper.newEmptyRequest(), media);
                        });

                    } else {
                        iLogManagerService.logError(this.getClass(), JOB_DESCRIPTION, product.getProductId(), responseObj.getString("message"));
                    }
                } else {
                    iLogManagerService.logError(this.getClass(), JOB_DESCRIPTION, product.getProductId(), response.message());
                }
            } catch (IOException e) {
                e.printStackTrace();
                iLogManagerService.logError(this.getClass(), JOB_DESCRIPTION, product.getProductId(), e.getMessage());
            }
        }

        iLogManagerService.logSuccess(this.getClass(), JOB_DESCRIPTION, null, "[" + count + "]同步成功，[" + (productList.size() - count) + "]条同步失败");
    }

    /**
     * 将图片组名称转化成JSON的属性名，如：RM_IMAGE -> rmIMAGEList
     *
     * @param groupName 组名称 类似于RM_IMAGE
     * @return JSON的属性名 如rmIMAGEList
     */
    private String transferGroupName(String groupName) {
        String[] strings = groupName.split(GROUP_NAME_SPLIT);
        return strings[0].toLowerCase() + org.apache.commons.lang3.StringUtils.capitalize(strings[1]) + GROUP_PROP_SUFFIX;
    }
}