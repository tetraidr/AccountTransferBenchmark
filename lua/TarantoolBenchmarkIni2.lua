package.path = '/root/TarantoolBenchmark2/?.lua;' .. package.path
box.cfg{
	background = false,
	custom_proc_title = "TarantoolBenchmark2",
	listen = 3301,
	memtx_dir = '/u01/TarantoolBenchmark2/memtx',
	pid_file = "/u01/TarantoolBenchmark2/tarantool.pid",
	read_only = false,
	wal_dir = "/u01/TarantoolBenchmark2/wal",
	work_dir = "/u01/TarantoolBenchmark2",
	log_level = 5,
}
box.schema.role.create('Tarantoolbenchmark', {if_not_exists = true})
box.schema.role.grant('Tarantoolbenchmark', 'execute, read, write', 'universe', nil, {if_not_exists = true})
box.schema.user.create('tester', {if_not_exists = true})
box.schema.user.passwd('tester', 'tester')
box.schema.user.grant('tester','Tarantoolbenchmark', nil, nil, {if_not_exists = true})
box.schema.space.create('BenchmarkSpace',{
	temporary = false,
	if_not_exists = true,
	engine = "memtx",
	user = "tester",
	format = {
		[1] = {["name"] = "Client_id"},
		[2] = {["type"] = "string"},
		[3] = {["name"] = "Account_state"},
		[4] = {["type"] = "unsigned"}
		}
	})
box.space.BenchmarkSpace:create_index('primary', {
	type = "HASH",
	unique = true,
	if_not_exists = true,
	parts = {
		1, "string"
		}
	})
cfg = {
	servers = {
		{uri = '10.36.8.68:3301', zone = '1'},
		{uri = '10.36.8.69:3301', zone = '2'},
		{uri = '10.36.8.70:3301', zone = '3'}
		},
	login = 'tester',
	password = 'tester',
	redundancy = 1,
	binary = 3301
	}
shard = require('shard')
connpull = require('connpool')
console = require('console')
shard.init(cfg)
Benchmark = require('TarantoolBenchmark')
console.start()
