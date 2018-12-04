import std.algorithm, std.format : formattedRead, formattedWrite;

struct Event {

	enum Type {
		start, asleep, awake
	}

	uint year, month, day, hour, minute, guard;
	Type type;

	this(scope const(char)[] line) {
		line.formattedRead!`[%d-%d-%d %d:%d] `(year, month, day, hour, minute);

		final switch(line[0 .. 5]) {
			case "Guard":
				line.formattedRead!"Guard #%d"(guard);
				type = Type.start;
				break;

			case "falls":
				type = Type.asleep;
				break;

			case "wakes":
				type = Type.awake;
				break;
		}
	}

	pure int opCmp(ref scope const Event other) const {
		return cmp(this.asRange, other.asRange);
	}

	private pure auto asRange() const {
		import std.range : only;

		return only(year, month, day, hour, minute);
	}

	pure uint opBinary(string op : "-")(ref scope const Event other) const {
		import std.math : abs;

		uint diff;

		if(day == other.day) {
			diff = abs(minute - other.minute);
		}
		else if(hour == 0) {
			diff = (60 - other.minute) + minute;
		} else {
			diff = (60 - minute) + other.minute;
		}

		return diff;
	}

	void toString(W)(ref W w) const {
		w.formattedWrite!`[%4d-%2d-%2d %2d:%2d] `(year, month, day, hour, minute);

		final switch(type) with(Type) {
			case start:
				w.formattedWrite!`Guard #%d begins shift`(guard);
				break;

			case awake:
				w.put("wakes up");
				break;

			case asleep:
				w.put("falls asleep");
				break;
		}
	}
}

void main() {
	import std.stdio;
	import std.array : array, byPair;

	const auto input = File("day4.in", "r").byLine // inputLines.splitter('\n')
		.map!(a => Event(a))
		.array
		.sort.release;

	uint currentGuard;
	size_t asleepEvent;
	
	alias Time = uint[60];

	Time[size_t] sleepTime;

	foreach(i, ref const event; input) {
		final switch(event.type) with(Event.Type) {
			case start:
				currentGuard = event.guard;
				break;

			case asleep:
				asleepEvent = i;
				break;

			case awake:

				const uint st = input[asleepEvent].minute;

				foreach(ref el; sleepTime.require(currentGuard, Time.init)[st .. event.minute]) {
					el++;
				}

				break;
		}
	}

	import std.range : enumerate;
	{
		auto entry = sleepTime.byPair.maxElement!"a.value[].sum";

		const minute = entry.value[].enumerate.maxElement!"a.value".index;

		writefln("1: %d", entry.key * minute);
	}

	// 2)
	{
		auto entry = sleepTime.byPair
			.map!("a.key", a => a.value[].enumerate.maxElement!"a.value")
			.maxElement!"a[1].value";

		writefln("2: %d", entry[0] * entry[1].index);
	}
}