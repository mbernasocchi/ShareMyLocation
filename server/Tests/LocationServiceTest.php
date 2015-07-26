<?php

namespace ShareMyLocation\server\Tests;



use ShareMyLocation\server\LocationService;
use ShareMyLocation\server\User;

class LocationServiceTest extends \PHPUnit_Framework_TestCase
{
    public function testUpdateLocationInvalidHash()
    {

        $testUser = $this->getTestUser();
        $locationService = new LocationService();

        $this->setExpectedException('\InvalidArgumentException', 'Incorrect Hash');

        $locationService->updateLocation($testUser, $this->getUserList());
    }

    public function testUpdateLocationOnExistingUser()
    {
        $locationService = new LocationService();
        $testUser = $this->getTestUser();

        $testUser->setUserName('Patrick');
        $testUser->setHash('96d13f73bfc59f8dc25fb222f33cbec7');
        $testUser->setTime(1436359258);

        $result = $locationService->updateLocation($testUser, $this->getUserList());

        $updatedEntry = json_decode($result, true)["userList"][0];

        $this->assertSame($updatedEntry["user"], "Patrick");
        $this->assertSame($updatedEntry["time"], 1436359258);
        $this->assertSame($updatedEntry["lat"], 111.11);
        $this->assertSame($updatedEntry["lon"], 222.22);
        $this->assertSame($updatedEntry["alt"], 333.33);
        $this->assertSame($updatedEntry["spd"], 444.44);
        $this->assertSame($updatedEntry["acc"], 100);
        $this->assertSame($updatedEntry["hash"], "96d13f73bfc59f8dc25fb222f33cbec7");
    }

    public function testUpdateLocationNewUser()
    {
        $locationService = new LocationService();
        $testUser = $this->getTestUser();
        $testUser->setUserName('NewUser');
        $testUser->setHash('c649fbde0f9e0d43cbf786ea34f08e2b');

        $result = $locationService->updateLocation($testUser, $this->getUserList());

        $newEntry = json_decode($result, true)["userList"][4];

        $this->assertSame($newEntry["user"], "NewUser");
        $this->assertSame($newEntry["time"], 123456);
        $this->assertSame($newEntry["lat"], 111.11);
        $this->assertSame($newEntry["lon"], 222.22);
        $this->assertSame($newEntry["alt"], 333.33);
        $this->assertSame($newEntry["spd"], 444.44);
        $this->assertSame($newEntry["acc"], 100);
        $this->assertSame($newEntry["hash"], "c649fbde0f9e0d43cbf786ea34f08e2b");

    }

    public function testUpdateLocationWithOldData()
    {
        $locationService = new LocationService();
        $testUser = $this->getTestUser();

        $testUser->setUserName('Patrick');
        $testUser->setHash('9983bb508feb4e4d28a2078ba84ef5f2');
        $testUser->setTime(1);

        $this->setExpectedException('\InvalidArgumentException', 'Old Data');

        $locationService->updateLocation($testUser, $this->getUserList());
    }

    private function getTestUser()
    {
        $testUser = new User();

        $testUser->setUserName('testUsername');
        $testUser->setTime(123456);
        $testUser->setLatitude(111.11);
        $testUser->setLongitude(222.22);
        $testUser->setAltitude(333.33);
        $testUser->setSpeed(444.44);
        $testUser->setAccuracy(100);
        $testUser->setHash('someHash');

        return $testUser;
    }

    private function getUserList()
    {
        return file_get_contents("testData.json", true);
    }
}