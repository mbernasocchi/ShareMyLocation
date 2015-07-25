<?php
$user = new \ShareMyLocation\server\User();
$locationService = new \ShareMyLocation\server\LocationService();

$user->setUserName($_GET['user']);

//the time is not saved in micro-seconds, so we have to divide by 1000 here
$user->setTime(round($_GET['time'])/1000);

$user->setLatitude($_GET['lat']);
$user->setLongitude($_GET['lon']);
$user->setAltitude($_GET['alt']);
$user->setSpeed($_GET['spd']);
$user->setAccuracy($_GET['acc']);
$user->setHash($_GET['hash']);


$jsonUserList = file_get_contents("location.json", true);

$updatedList = $locationService->updateLocation($user, $jsonUserList);

$locationFile = fopen("location.json", "w+") or die("Unable to open file!");

fwrite($locationFile, $updatedList);
fclose($locationFile);

