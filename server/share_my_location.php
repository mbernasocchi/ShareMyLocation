<?php
//CONFIGURE SAME AS IN APP
$HASH_SALT = '1234';
//END CONFIGURE

$fix_time = $_GET['time'];
$latitude = $_GET['lat'];
$longitude = $_GET['lon'];
$altitude = $_GET['alt'];
$speed = $_GET['spd'];
$accuracy = $_GET['acc'];
$hash = $_GET['hash'];

$data = $fix_time . $latitude . $longitude . $altitude . $speed . $accuracy . $HASH_SALT;
$md5 = md5($data);

$string = file_get_contents("location.json");
$json=json_decode($string,true);
$last_json_time = $json['time'];

if ($md5 != $_GET['hash']){
    die("invalid hash");
}
else if($last_json_time > $fix_time){
    die("old data");
}
else{
    $myfile = fopen("location.json", "w") or die("Unable to open file!");
    $format = '{"time":"%s","lat":%s,"lon":%s,"alt":%s,"spd":%s,"acc":%s,"hash":"%s"}';
    $json = sprintf($format, $fix_time, $latitude, $longitude, $altitude, $speed, $accuracy, $hash);
    fwrite($myfile, $json);
    print "OK";
}
fclose($myfile);

?>