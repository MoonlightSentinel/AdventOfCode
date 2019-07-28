module aoc.y2016.d05;

import std;
@safe:

void main() {
	const input = "uqwqemis";
	const a = solveA(input);
	const b = solveB(input);
	writefln!"a: %s\nb: %s"(a, b);
}

pure:

auto solveA(scope const char[] input) {
	uint idx = 0;
	
	return input.solve!((pass, hash) => pass[idx++] = hash[5]);
}

unittest {
	assert("18F47A30" == solveA("abc"));
}

auto solveB(scope const char[] input) {
	
	return input.solve!((pass, hash) {
		const idx = hash[5] - '0';
		const val = hash[6];
		
		if(idx < 0 || idx > 7 || pass[idx] != char.init) {
			return false;
		}		
		
		debug writefln!"pass[%s] = %s"(idx, val);

		pass[idx] = val;
		
		return true;
	});
}

unittest {
	assert("05ACE8E3" == solveB("abc"));
}

auto solve(alias addChar)(scope const char[] input) {
	char[] result = new char[8];
	ulong idx = 0;
	
	debug writeln();
	
	foreach(_; 0 .. result.length) {
		while(true) {
			import std.digest.md;
			
			const str = input ~ to!string(idx++);
			const hash = hexDigest!MD5(str);
			
			if(hash[0..5] == "00000") {
				debug writefln!"String  : %s\nHash    : %s"(str, hash);
				if(addChar(result, hash)) {
					debug {
						write("Password: ");
						foreach(const ch; result) {
							write(ch == char.init ? '_' : ch);
						}
						writeln('\n');
					}
					break;
				}
				
				debug writeln('\n');
			}
		}
	}
	
	return result;
}