module day16;

import std.algorithm;

void main() {
	version(unittest) {} else {
		import std.stdio;

		auto input = File("day16.in", "r").byLine.parse;
		
		with(solve(input)) {
			writefln!"1: %d\n2: %d"(a, b);
		}
	}
}

unittest {
	import std.stdio : File;

	auto input = File("day16.in", "r").byLine.parse;
	
	with(solve(input)) {
		assertEquals(a, 651);
	}
}

struct Input {
	Example[] examples;
	Instruction[] program;
}
	
Input parse(R)(R range) {
	import std.array : array;
	import std.format : formattedRead;
	import std.range.primitives : empty, front, popFront;
	
	Input input;

	while(!range.front.empty) {

		input.examples ~= parseExample(range);

		// Padding
		range.popFront();
	}
	
	// Skip padding between input for a and b
	while(range.front.empty) range.popFront();
	
	while(!range.empty) {
		Instruction i;
		
		range.front.formattedRead!"%d %d %d %d"(i.op, i.a, i.b, i.c);
		range.popFront();
		
		input.program ~= i;
	}

	return input;
}

Example parseExample(R)(R range) {
	import std.format : formattedRead;
	import std.range.primitives : empty, front, popFront;

	Example ex;

	range.front.formattedRead!"Before: [%(%d, %)]"(ex.before);
	range.popFront();

	range.front.formattedRead!"%d %d %d %d"(ex.op, ex.a, ex.b, ex.c);
	range.popFront();

	range.front.formattedRead!"After:  [%(%d, %)]"(ex.after);
	range.popFront();

	return ex;
}

struct Instruction {
	uint op, a, b, c;
}

alias Registers = uint[4];

struct Operators {
	static uint _calc(string what, bool aReg, bool bReg)(const ref Registers reg, const uint opA, const uint opB) {

		static if(aReg) const a = reg[opA];
		else alias a = opA;

		static if(bReg) const b = reg[opB];
		else alias b = opB;

		return mixin(what);
	}

	static foreach(const type; ['r', 'i']) {
		static foreach(const op, const name; ["+": "add", "*": "mul", "&": "ban", "|": "bor"]) {
			mixin("alias " ~ name ~ type ~ ` = _calc!("a ` ~ op ~ ` b", true, type == 'r');`);
		}
		
		mixin("alias set" ~ type ~ ` = _calc!("a", type == 'r', false);`);
		
		static foreach(const op, const name; ["==": "eq", ">": "gt"]) {
			static foreach(type2; ['r', 'i']) {
				static if(type != 'i' || type2 != 'i') {
					mixin(`alias ` ~ name ~ type ~ type2 ~ ` = _calc!("a ` ~ op ~ ` b ? 1 : 0", type == 'r', type2 == 'r');`);
				}
			}
		}
	}
}

import std.array : join;

mixin(`enum OpCode { ` ~ [__traits(allMembers, Operators)].filter!"a[0] != '_'".join(',') ~ '}');

pure uint calc(const OpCode code, ref const Registers reg, const uint a, const uint b) {

	final switch(code) {
		static foreach(const codeStr;  __traits(allMembers, OpCode)) {
			case mixin("OpCode." ~ codeStr):
				return mixin("Operators." ~ codeStr)(reg, a, b);
		}
	}
}

struct Example {
	Registers before, after;
	Instruction instruction;
	alias instruction this;

	void toString(W)(scope ref W writer) const {
		import std.format : formattedWrite;

		enum fmt = "Before: [%(%d, %)]\n%d %d %d %d\nAfter:  [%(%d, %)]";
		writer.formattedWrite!fmt(before, op, a, b, c,after);
	}
	
	pure OpCode[] findPotentialOperators() const {
		import std.traits : EnumMembers;

		OpCode[] valid;
		
		foreach(const code; EnumMembers!OpCode) {
			if(calc(code, before, a, b) == after[c]) {
				valid ~= code;
			}
		}
		
		return valid;
	}
	
	unittest {
		static immutable inputStr = [
			"Before: [3, 2, 1, 1]",
			"9 2 1 2",
			"After:  [3, 2, 2, 1]",
			"", "",
			"0 0 0 0"
		];

		const input = parseExample(inputStr.dup);

		const ops = input.findPotentialOperators();

		assertEquals(ops.length, 3);
	}
}

auto solve(const Input input) {
	import std.array : array;

	struct Result {
		uint a, b;
	}
	
	Result result;

	OpCode[uint] codes;
	
	{
		auto potOperators = input.examples.map!"a.findPotentialOperators".array;
		
		result.a = potOperators.filter!"a.length >= 3".count;
		uint[OpCode] codesRev;
		
		while(codes.length < __traits(allMembers, OpCode).length) {
		
			foreach(const i, ref ex; potOperators) {
				if(ex.length == 1) {
					const found = ex[0];
					const op =  input.examples[i].op;
					
					if(auto already = op in codes) {
						assert(*already == found, "Overwriting code!");
					}
					
					else {
						codes[op] = found;
						codesRev[found] = op;
					}
				}
			}
			
			foreach(ref ex; potOperators) {
				ex = ex.filter!(v => v !in codesRev).array;
			}
		}
	}
	
	Registers reg;
	
	foreach(const instr; input.program) {
		with(instr) {
			reg[c] = calc(codes[op], reg, a, b);
		}
	}
	
	result.b = reg[0];
	
	return result;
}

version(unittest)
void assertEquals(A,B)(scope auto ref const A a, scope auto ref const B b) {
	import std.format : format;

	assert(a == b, format!"Expected %s but got %s"(b, a));
}