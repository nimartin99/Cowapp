#!/bin/bash

TEST=`grep "With group packages starting for teams of four all the way up to 20, no other travel provider gets your group the distance" 'Teams In Space.html'`

if [ -n "${TEST}" ]; then
  exit 0
fi


echo "FAIL - incorrect text"
exit 1

