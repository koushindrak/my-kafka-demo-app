#!/bin/bash

# Define variables
CLUSTER_NAME="ktable-cluster"
REGION="us-east-1"

# Delete the EKS cluster using eksctl
eksctl delete cluster --name $CLUSTER_NAME --region $REGION

# Clean up the manually created EBS volume
# First, we'll list the volumes and then delete them. Be cautious with this step to ensure you're not deleting any unrelated volumes.
VOLUME_IDS=$(aws ec2 describe-volumes --region $REGION --filters Name=availability-zone,Values=us-east-1a Name=size,Values=10 Name=volume-type,Values=gp2 --query "Volumes[*].[VolumeId]" --output text)

for volume in $VOLUME_IDS; do
    echo "Deleting volume: $volume"
    aws ec2 delete-volume --volume-id $volume --region $REGION
done

echo "Cleanup complete!"
