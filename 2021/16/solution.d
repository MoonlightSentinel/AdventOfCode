module y2021.d16.solution;

void main()
{
	import std.stdio;

	const line = File("input.txt", "r")
						.byLine
						.front;

	const input = parse(line);
	const ulong[2] res = solve(input);

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

ubyte[] parse(const char[] line)
{
	// assert(line.length % 2 == 0);

	// ubyte[] result = new ubyte[](line.length / 2);
	// const(char)* cur = line.ptr;

	// static ubyte toByte(const char ch)
	// {
	// 	if (ch <= '9')
	// 		return cast(ubyte) (ch - '0');
	// 	else
	// 		return cast(ubyte) (ch - 'A');
	// }

	// foreach (ref entry; result)
	// {
	// 	const a = toByte(*(cur++));
	// 	const b = toByte(*(cur++));
	// 	entry = (cast(ubyte) (a << 4)) | b;
	// }


	static ubyte[] toDigits(const char ch)
	{
		uint val = ch <= '9' ? ch - '0' : (10 + ch - 'A');

		ubyte[4] result;
		uint idx = result.length;

		while (val)
		{
			result[--idx] = val & 1;
			val >>= 1;
		}

		return result.dup;
	}

	ubyte[] result;
	result.reserve(4 * line.length);

	foreach (const digit; line)
	{
		Switch:
		final switch (digit)
		{
			static foreach (const char ch; "0123456789ABCDEF")
			{
				case ch:
				{
					static immutable ubyte[] bits = toDigits(ch);
					result ~= bits;
					break Switch;
				}
			}
		}
	}

	return result;
}

unittest
{
	const ubyte[] act = parse("D2FE28");
	const ubyte[] exp = [ 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0 ];
	assert(act == exp);
}

ulong[2] solve(const ubyte[] bits)
{
	import std.algorithm;

	static uint fromBinary(const ubyte[] bits)
	{
		uint result;
		foreach (const bit; bits)
			result = (result << 1) | bit;
		return result;
	}

	ulong[2] result;
	size_t idx;

	uint readBit()
	{
		return bits[idx++];
	}

	uint readBits(size_t count)
	{
		const selected = bits[idx .. idx + count];
		idx += count;
		return fromBinary(selected);
	}

	ulong parsePackage()
	{
		const packetVer = readBits(3);
		result[0] += packetVer;
		// writeln("Version = ", packetVer);

		const packetType = readBits(3);
		// writeln("Type = ", packetType);

		ulong function(ulong[]) combine = (_) { assert(false, "Invalid operator"); };

		switch (packetType)
		{
			// Literal value
			case 4:
			{
				ulong number;
				bool more;
				do
				{
					more = readBit() == 1;
					foreach (const _; 0 .. 4)
						number = (number << 1) | readBit();
				}
				while (more);

				return number;
			}

			// Select operator based on type number
			case 0: combine = arr => sum(arr);				goto default;
			case 1: combine = arr => reduce!"a * b"(arr);	goto default;
			case 2: combine = arr => minElement(arr);		goto default;
			case 3: combine = arr => maxElement(arr);		goto default;
			case 5: combine = arr => arr[0] > arr[1];		goto default;
			case 6: combine = arr => arr[0] < arr[1];		goto default;
			case 7: combine = arr => arr[0] == arr[1];		goto default;

			default: // Operator
			{
				ulong[] results;

				const lenIsBits = !readBit();

				if (lenIsBits)
				{
					const length = readBits(15); // writeln(length, " bits");
					const end = idx + length;
					while (idx < end)
						results ~= parsePackage();
				}
				else
				{
					auto count = readBits(11); // writeln(count, " packages");
					results.reserve(count);
					while (count--)
						results ~= parsePackage();
				}
				return combine(results);
			}
		}
	}

	result[1] = parsePackage();

	return result;
}

unittest
{
	static void check(string input, const ulong a = -1, const ulong b = -1)
	{
		const res = solve(parse(input));
		if (a != -1)
			assert(res[0] == a);
		if (b != -1)
			assert(res[1] == b);
	}

	static int test()
	{
		check("D2FE28", 6);
		check("38006F45291200", 9);
		check("EE00D40C823060", 14);

		check("8A004A801A8002F478", 16);
		check("620080001611562C8802118E34", 12);
		check("C0015000016115A2E0802F182340", 23);
		check("A0016C880162017C3686B18A3D4780", 31);

		check("C200B40A82", -1, 3);
		check("04005AC33890", -1, 54);
		check("880086C3E88112", -1, 7);
		check("CE00C43D881120", -1, 9);
		check("D8005AC2A8F0", -1, 1);
		check("F600BC2D8F", -1, 0);
		check("9C005AC2F8F0", -1, 0);
		check("9C0141080250320F1802104A08", -1, 1);

		return 0;
	}

	test();
	enum force = test();
}
