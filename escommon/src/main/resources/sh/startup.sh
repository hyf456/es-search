#!/bin/sh

ulimit -n 75536

sysctl -w vm.max_map_count=522144

su hub

./app/elasticsearch-5.5.2/bin/elasticsearch -d
