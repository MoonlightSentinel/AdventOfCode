import std.algorithm;

void main() {
	version(unittest){} else {

		import std.stdio;

		auto input = File("day7.in", "r").byLine.parse;

		writefln("1: %s", part1(input.dup));
		writefln("2: %s", part2(input.dup));
	}
}

struct Task {
	int[] successors;
	int numPredecessors;

	this(scope ref const Task other) {
		this.numPredecessors = other.numPredecessors;
		this.successors = other.successors.dup;
	}
}

Task[] parse(R)(R input) {

	Task[] tasks;

	foreach(line; input) {
		enum nodeIdx = "Step ".length,
			sucIdx = "Step _ must be finished before step ".length;

		const node = line[nodeIdx]  - 'A';
		const suc =  line[sucIdx] - 'A';

		const m = max(node, suc);

		if(m >= tasks.length) {
			tasks.length = m + 1;
		}

		tasks[node].successors ~= suc;
		tasks[suc].numPredecessors++;
	}

	debug {
		import std.stdio;
		
		writeln("Input:");
		foreach(const i, const task; tasks) with(task) {
			writefln!"%c (%d) -> %s"(i.asChar, numPredecessors, successors);
		}
		writeln();
	}

	return tasks;
}

// Kahn's Algorithm
char[] part1(Task[] tasks) {

	char[] result;
	result.reserve(tasks.length);

	while(result.length < tasks.length) {

		const idx = tasks.countUntil!"a.numPredecessors == 0"();
		auto node = &(tasks[idx]);

		node.numPredecessors = -1;

		result ~= cast(char) (idx + 'A');

		foreach(const suc; node.successors) {
			tasks[suc].numPredecessors--;
		}
	}

	return result;
}

uint part2(Task[] tasks) {
	
	struct Worker {
		int taskId = -1, 
			remainingTime = 0;
	}

	version(unittest) {
		enum NUM_WORKERS = 2;
		enum TIME_BONUS = 0;
	} else {
		enum NUM_WORKERS = 5;
		enum TIME_BONUS = 60;
	}

	Worker[NUM_WORKERS] workers;

	uint timePassed = 0, nextTime = 0, done = 0;

	debug char[] doneChars;

	while(true) {
		
		timePassed += nextTime;

		foreach(ref worker; workers) with(worker) {

			if((remainingTime -= nextTime) <= 0 && taskId != -1) {

				foreach(const succ; tasks[taskId].successors) {
					tasks[succ].numPredecessors--;
				}

				debug doneChars ~= taskId.asChar();
				taskId = -1;
				done++;
			}
		}

		foreach(ref worker; workers) with(worker) {

			if(taskId == -1) {

				const availiableTask = tasks.countUntil!"a.numPredecessors == 0"();

				if(availiableTask == -1) {
					taskId = -1;
					break;
				} else {
					taskId = availiableTask;
					remainingTime = availiableTask + TIME_BONUS + 1;
					tasks[availiableTask].numPredecessors = -1;
				}
			}
		}

		debug {
			import std.stdio : writefln;
			
			writefln!"%6d  |  %(%c    %)  |  %s"(timePassed, workers[].map!(w => w.taskId.asChar), doneChars);
		}
		
		if(done == tasks.length) {
			return timePassed;
		}

		nextTime = workers[].map!"a.remainingTime".filter!"a > 0".minElement;
	}
}

char asChar(int task) {
	return task < 0 ? '.' : cast(char) ('A' + task);
}

unittest {
	static immutable exampleStr = [
		"Step C must be finished before step A can begin.",
		"Step C must be finished before step F can begin.",
		"Step A must be finished before step B can begin.",
		"Step A must be finished before step D can begin.",
		"Step B must be finished before step E can begin.",
		"Step D must be finished before step E can begin.",
		"Step F must be finished before step E can begin."
	];

	auto example = parse(exampleStr);

	assertEquals(part1(example.dup), "CABDFE");
	assertEquals(part2(example.dup), 15);
}
version(unittest)
void assertEquals(A, B)(const A got, const B expected) {
	import std.format : format;

	assert(got == expected, format!"Expected %s but got %s!"(expected, got));
}