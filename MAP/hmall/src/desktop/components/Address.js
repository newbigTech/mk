let provinces,
  citysMap = {},
  districtsMap = {};

function getProvinces(callback) {
  if(provinces){
    callback(provinces);
  }else{
    fetch(Hmall.cdnPath('/data/provinces.json'))
    .then(Hmall.convertResponse('json',this))
    .then(json => {
      callback(provinces = json);
    })
    .catch(Hmall.catchHttpError(() => {
      callback({});
    }));
  }
}

function getCitys(province_code, callback) {
  let citys = province_code ? citysMap[province_code] : {};
  if(citys){
    callback(citys);
  }else{
    fetch(Hmall.cdnPath(`/data/city/${province_code}.json`))
    .then(Hmall.convertResponse('json',this))
    .then(json => {
      callback(citysMap[province_code] = json);
    })
    .catch(Hmall.catchHttpError(() => {
      callback({});
    }));
  }
}

function getDistricts(city_code, callback) {
  let districts = city_code ? districtsMap[city_code] : {};
  if(districts){
    callback(districts);
  }else{
    fetch(Hmall.cdnPath(`/data/district/${city_code}.json`))
    .then(Hmall.convertResponse('json',this))
    .then(json => {
      callback(districtsMap[city_code] = json);
    })
    .catch(Hmall.catchHttpError(() => {
      callback({});
    }));
  }
}

export {
  getProvinces,
  getCitys,
  getDistricts
};