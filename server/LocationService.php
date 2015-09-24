<?php

namespace ShareMyLocation\server;

use ShareMyLocation\server\User;

class LocationService
{

    const HASH_SALT = '1234';

    public function updateLocation(User $user, $jsonUserList)
    {
        if (!$this->hashIsValid($user)) {
            throw new \InvalidArgumentException('Incorrect Hash');
        }

        $userList = json_decode($jsonUserList, true)["userList"];

        foreach ($userList as &$entry) {
            if ($entry["user"] == $user->getUserName()) {
                if($entry['time'] > $user->getTime()) {
                    throw new \InvalidArgumentException('Old Data');
                }
                $entry["time"] = $user->getTime();
                $entry["lat"] = $user->getLatitude();
                $entry["lon"] = $user->getLongitude();
                $entry["alt"] = $user->getAltitude();
                $entry["spd"] = $user->getSpeed();
                $entry["acc"] = $user->getAccuracy();
                $entry["hash"] = $user->getHash();

                return json_encode(array("userList"=>$userList), JSON_PRETTY_PRINT);
            }
        }

        array_push(
            $userList, array(
                'time' => $user->getTime(),
                'lat' => $user->getLatitude(),
                'lon' => $user->getLongitude(),
                'alt' => $user->getAltitude(),
                'spd' => $user->getSpeed(),
                'acc' => $user->getAccuracy(),
                'hash' => $user->getHash(),
                'user' => $user->getUserName(),
            )
        );


        return json_encode(array("userList"=>$userList), JSON_PRETTY_PRINT);
    }

    private function hashIsValid(User $user)
    {
        return md5(
            $user->getTime().
            $user->getLatitude().
            $user->getLongitude().
            $user->getAltitude().
            $user->getSpeed().
            $user->getAccuracy().
            $user->getUserName().
            self::HASH_SALT
        ) == $user->getHash();
    }
}