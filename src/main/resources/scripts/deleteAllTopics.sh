for topic in $(/Users/koushindrakumar/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092); do
bin/kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic $topic;
 done