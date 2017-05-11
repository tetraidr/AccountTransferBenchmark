local exports = {}
exports.FillRndAccounts = function(number)
	local CharSet = 'qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890'
	local name = ''
	local value = ''
	for i = 1,number do
		name = string.random(8,CharSet)
		value = math.random(1,1000000)
		print(name,value)
		shard.BenchmarkSpace:insert{name, value}
	end
end
local Chars = {}
for Loop = 0, 255 do
   Chars[Loop+1] = string.char(Loop)
end
local String = table.concat(Chars)

local Built = {['.'] = Chars}

local AddLookup = function(CharSet)
   local Substitute = string.gsub(String, '[^'..CharSet..']', '')
   local Lookup = {}
   for Loop = 1, string.len(Substitute) do
       Lookup[Loop] = string.sub(Substitute, Loop, Loop)
   end
   Built[CharSet] = Lookup

   return Lookup
end
function string.random(Length, CharSet)
   -- Length (number)
   -- CharSet (string, optional); e.g. %l%d for lower case letters and digits

   local CharSet = CharSet or '.'

   if CharSet == '' then
      return ''
   else
      local Result = {}
      local Lookup = Built[CharSet] or AddLookup(CharSet)
      local Range = #Lookup

      for Loop = 1,Length do
         Result[Loop] = Lookup[math.random(1, Range)]
      end

      return table.concat(Result)
   end
end
function get_uuid()
	local fh=assert(io.popen'uuidgen')
	local uuid = fh:read()
	fh:close()
	return uuid
end
exports.AcctTransfer = function(args)
	Acct1_obj = shard.BenchmarkSpace:select{args[1]}
	Acct2_obj = shard.BenchmarkSpace:select{args[2]}
	if not ((Acct1_obj[1][1] == nil) or (Acct2_obj[1][1] == nil)) then
		--if (Acct1_obj[1][1][2] - args[3] > 0) and not (Acct1_obj[1][1][1] == Acct2_obj[1][1][1]) then
		if not (Acct1_obj[1][1][1] == Acct2_obj[1][1][1]) then
			local res = shard.BenchmarkSpace:update({Acct1_obj[1][1][1]}, {{'-', 2, tonumber(args[3])}})
			if (0<=tonumber(res[1][2])) then
				shard.BenchmarkSpace:update({Acct2_obj[1][1][1]}, {{'+', 2, tonumber(args[3])}})
				return 1
			else
				shard.BenchmarkSpace:update({Acct1_obj[1][1][1]}, {{'+', 2, tonumber(args[3])}})
				return 0
			end
		end
	end
	return 0
end
--[[exports.AcctTransfer = function(args)
	box.begin();
	Acct1_obj = shard.BenchmarkSpace:select{args[1]}
	Acct2_obj = shard.BenchmarkSpace:select{args[2]}
	id1 = get_uuid()
	id2 = get_uuid()
	if not ((Acct1_obj[1][1] == nil) or (Acct2_obj[1][1] == nil)) then
		if (Acct1_obj[1][1][2] - args[3] > 0) and not (Acct1_obj[1][1][1] == Acct2_obj[1][1][1]) then
			batch_obj = shard.q_begin()
			batch_obj.BenchmarkSpace:q_update(id1, Acct1_obj[1][1][1], {{'=', 2, tonumber(Acct1_obj[1][1][2]) - tonumber(args[3])}})
			batch_obj.BenchmarkSpace:q_update(id2, Acct2_obj[1][1][1], {{'=', 2, tonumber(Acct2_obj[1][1][2]) + tonumber(args[3])}})
			batch_obj:q_end()
			box.commit()
			return 1
		end
	end
	box.commit()
	return 0
end]]--
exports.AddAccount = function(args)
	--print(args[1])
	--print(args[2])
	shard.BenchmarkSpace:insert(args)
	return nil
end
exports.checksum = function()
	local result = 0
	for k, v in pairs(shard.shards) do
		c = v[1].conn:timeout(10)
		result = result + tonumber(c['call'](c,'Benchmark.checksumlocal',{}))
	end
	return result
end
exports.checksumlocal = function()
	local result = 0
	for k,v in box.space.BenchmarkSpace:pairs() do
		result = result + tonumber(v[2])
	end
	return result
end
exports.drop_space = function()
	box.space.cluster_manager:auto_increment{0,'drop_space','BenchmarkSpace'}
end
return exports