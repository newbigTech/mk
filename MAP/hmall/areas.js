var path = require('path'),
  fs = require('fs'),
  areas = require(path.resolve('areas.json')),
  export_path = path.join('src','main','webapp','data'),
  city_path = path.join(export_path,'city'),
  district_path = path.join(export_path,'district'),
  provinces = {};

function writeFile(dir,filename,data){
  fs.exists(path.resolve(dir),function(exists){
    function write(){
      fs.writeFile(path.resolve(dir,filename),JSON.stringify(data),{flag: 'w+', encoding: 'utf8'});
    }
    if(exists){
      write();
    }else{
      fs.mkdir(path.resolve(dir), function (err) {
        if(err)
          throw err;
        write();
      });
    }
  });
}

Object.keys(areas).forEach((province_code) => {
  var province = areas[province_code],
    citys = province.city,
    _citys = {};
  provinces[province_code] = province.name;
  Object.keys(citys).forEach((city_code) => {
    var city = citys[city_code],
      districts = city.district;
    _citys[city_code] = city.name;
    writeFile(district_path,city_code+'.json',districts);
  });
  writeFile(city_path,province_code+'.json',_citys);
});
writeFile(export_path,'provinces.json',provinces);