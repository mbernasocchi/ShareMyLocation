<?php

namespace ShareMyLocation\server\Tests;


use ShareMyLocation\server\User;

class UserTest extends \PHPUnit_Framework_TestCase
{
    public function testGettersAndSetters()
    {
        $user = new User();

        $user->setUserName('Username');
        $user->setTime('123456');
        $user->setLatitude(111.11);
        $user->setLongitude(222.22);
        $user->setAltitude(333.33);
        $user->setSpeed(444.44);
        $user->setAccuracy(100);
        $user->setHash('someHash');

        $this->assertSame('Username', $user->getUserName());
        $this->assertSame('123456', $user->getTime());
        $this->assertSame(111.11, $user->getLatitude());
        $this->assertSame(222.22, $user->getLongitude());
        $this->assertSame(333.33, $user->getAltitude());
        $this->assertSame(444.44, $user->getSpeed());
        $this->assertSame(100, $user->getAccuracy());
        $this->assertSame('someHash', $user->getHash());
    }
}
