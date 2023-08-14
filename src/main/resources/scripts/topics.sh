bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 \
 --partitions 1 --topic input-topic

# create intermediary log compacted topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 \
--partitions 1 --topic temp-topic --config cleanup.policy=compact

# create output log compacted topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 \
--partitions 1 --topic output-topic --config cleanup.policy=compact

# list topics
bin/kafka-topics.sh --list --bootstrap-server localhost:9092

####### for quick compaction
#For creating a new compacted topic with configurations that would make compaction happen quickly:
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --topic output-topic \
--partitions 1 --replication-factor 1 \
--config cleanup.policy=compact \
--config segment.bytes=1048576 \
--config min.cleanable.dirty.ratio=0.01 \
--config segment.ms=10000 \

# For modifying an existing topic:

bin/kafka-configs.sh --bootstrap-server localhost:9092 --entity-type topics \
--entity-name temp-topic \
--alter \
--add-config "cleanup.policy=compact,segment.bytes=1048576,min.cleanable.dirty.ratio=0.01,segment.ms=10000"
