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
$user = $_GET['user'];
$hash = $_GET['hash'];

$data = $fix_time . $latitude . $longitude . $altitude . $speed . $accuracy . $user . $HASH_SALT;
$md5 = md5($data);

$fix_time = round($fix_time/1000);

$string = file_get_contents("location.json");
$json = json_decode($string, true)["userList"];


if ($md5 != $_GET['hash']){
    die("invalid hash");
}

$i = 0;
$found = false;
foreach ($json as $v) {
   if ( $json[$i]["user"] == $user ) {
	$last_json_time = $json[$i]['time'];
	if($last_json_time > $fix_time){
	    die("old data");
	}
        $json[$i]["time"] = $fix_time;
        $json[$i]["lat"] = $latitude;
        $json[$i]["lon"] = $longitude;
        $json[$i]["alt"] = $altitude;
        $json[$i]["spd"] = $speed;
        $json[$i]["acc"] = $accuracy;
        $json[$i]["hash"] = $hash;
	$found = true;
   }
   $i++;
}

   if ( $found == false ) {
	echo "new user";
	array_push(
		$json, array(
			'time' => $fix_time,
			'lat' => $latitude,
			'lon' => $longitude,
			'alt' => $altitude,
			'spd' => $speed,
			'acc' => $accuracy,
			'hash' => $hash,
			'user' => $user,
		)
	);
   }

$string = json_encode(array("userList"=>$json), JSON_PRETTY_PRINT);


$myfile = fopen("location.json", "w+") or die("Unable to open file!");
fwrite($myfile, $string);
fclose($myfile);
    print "OK";

?>
