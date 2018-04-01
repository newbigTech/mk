product:

1. 商品信息的相关实现

2. ProductController
   2.1 selectProductByCode             根据据产品code查询产品
   2.2 selectProductByCodes            根据code集合新增商品批量查询

3. HapProductForDroolsController

   3.1 用于HAP查询商品信息
       3.1.1 selectAllProduct
       3.1.2 selectByOmsProductId
       3.1.3 queryByProductIds

   3.2 查询商品
       3.2.1 selectSkuByNotIn
       3.2.2 selectSpuByNotIn
       3.2.3 selectByProductIds

   3.3 queryByNotInAndCount             用于hap查询已选择赠品

   3.4 selectGift                       hap中查询促销可用赠品

   3.5 checkedCouponCount               促销赠品数量校验