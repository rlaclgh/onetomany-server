pkill -f "java -jar /home/ubuntu/deploy/build/libs/onetomany-0.0.1-SNAPSHOT.jar"

#pgrep -f "java -jar /home/ubuntu/deploy/build/libs/onetomany-0.0.1-SNAPSHOT.jar"

nohup java -jar /home/ubuntu/deploy/build/libs/onetomany-0.0.1-SNAPSHOT.jar > nohup.log 2>&1 &