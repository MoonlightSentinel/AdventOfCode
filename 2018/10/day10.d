import std.algorithm;

void main() {
	version(unittest){} else {
		import std.stdio;

		File("day10.in", "r")
			.byLine
			.parse
			.solve();
	}
}

auto parse(R)(R range) {
	import std.array : array;

	return range.map!(a => Point(a)).array;
}

struct Point {
	int x, y, xVelocity, yVelocity;

	this(scope const(char)[] line) {
		import std.format : formattedRead;
		import std.ascii : isWhite;

		enum fmt = "position=<%d,%d> velocity=<%d,%d>";

		line.filter!(c => !isWhite(c))
			.formattedRead!fmt(x, y, xVelocity, yVelocity);
	}

	void step() {
		this.x += this.xVelocity;
		this.y += this.yVelocity;
	}
}

void solve(Point[] points) {
	import std.stdio;

	Point[] backup = new Point[points.length];
	ulong backupSize = ulong.max;
	uint counter = 0;

	while(true) {
		points.each!"a.step()"();

		const size = Dimensions(points).size();

		if(size > backupSize) {
			break;
		}

		counter++;

		backup[] = points[];
		backupSize = size;
	}

	print(backup);
	writefln!"%d Seconds!"(counter);
}

struct Dimensions {
	int minX, minY, maxX, maxY;

	this(const Point[] points) {
		minX = points.map!"a.x".minElement,
		minY = points.map!"a.y".minElement,
		maxX = points.map!"a.x".maxElement + 1,
		maxY = points.map!"a.y".maxElement + 1;
	}

	ulong size() const {
		import std.math : abs;

		return (cast(ulong) abs(maxX - minX)) * abs(maxY - minY);
	}
}

void print(const Point[] points) {
	import std.stdio : write, writeln;

	with(Dimensions(points)) {
		foreach(const y; minY .. maxY) {

			foreach(const x; minX .. maxX) {

				write(points.canFind!(p => p.x == x && p.y == y) ? '#' : '.');
			}

			write('\n');
		}
	}
	writeln('\n');
}

unittest {
	static immutable exampleStr =
"position=< 9,  1> velocity=< 0,  2>
position=< 7,  0> velocity=<-1,  0>
position=< 3, -2> velocity=<-1,  1>
position=< 6, 10> velocity=<-2, -1>
position=< 2, -4> velocity=< 2,  2>
position=<-6, 10> velocity=< 2, -2>
position=< 1,  8> velocity=< 1, -1>
position=< 1,  7> velocity=< 1,  0>
position=<-3, 11> velocity=< 1, -2>
position=< 7,  6> velocity=<-1, -1>
position=<-2,  3> velocity=< 1,  0>
position=<-4,  3> velocity=< 2,  0>
position=<10, -3> velocity=<-1,  1>
position=< 5, 11> velocity=< 1, -2>
position=< 4,  7> velocity=< 0, -1>
position=< 8, -2> velocity=< 0,  1>
position=<15,  0> velocity=<-2,  0>
position=< 1,  6> velocity=< 1,  0>
position=< 8,  9> velocity=< 0, -1>
position=< 3,  3> velocity=<-1,  1>
position=< 0,  5> velocity=< 0, -1>
position=<-2,  2> velocity=< 2,  0>
position=< 5, -2> velocity=< 1,  2>
position=< 1,  4> velocity=< 2,  1>
position=<-2,  7> velocity=< 2, -2>
position=< 3,  6> velocity=<-1, -1>
position=< 5,  0> velocity=< 1,  0>
position=<-6,  0> velocity=< 2,  0>
position=< 5,  9> velocity=< 1, -2>
position=<14,  7> velocity=<-2,  0>
position=<-3,  6> velocity=< 2, -1>";

	exampleStr.splitter('\n')
		.parse()
		.solve();
}

version(unittest)
void assertEquals(A, B)(const A got, const B expected) {
	import std.format : format;

	assert(got == expected, format!"Expected %s but got %s!"(expected, got));
}