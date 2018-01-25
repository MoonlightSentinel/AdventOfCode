defmodule Day13 do

  def bits(cur, sum \\ 0) do
    if cur == 0 do
      sum
    else
      div(cur, 2) |> bits(sum + rem(cur, 2))
    end
  end

  def x(pos), do: elem(pos, 0)
  def y(pos), do: elem(pos, 1)

  def isSpace(pos, key) do
    x = x(pos)
    y = y(pos)
    x*x + 3*x + 2*x*y + y + y*y + key
    |> bits
    |> rem(2) == 0
  end

  def diff(a, b) do
    if a < b do b - a else a - b end
  end

  def eval(cur, target) do
    diff(x(cur), x(target)) + diff(y(cur), y(target))
  end

  def neighbours(cur) do
    x = x(cur)
    y = y(cur)
    [
      {x+1,y},
      {x,y+1},
      {x-1,y},
      {x,y-1}
    ]
  end

  def aSearch(target, key), do: aSearch(target, key, 0, -1, [{1,1}])

  # Found result
  def aSearch(target, _, sum, _, path) when hd(path) == target, do: sum

  # Cut branch
  def aSearch(_, _, _, sum, limit, _) when limit != -1 and sum >= limit, do: -1

  # Continue searching
  def aSearch(target, key, sum, limit, path) do
    #IO.inspect([target, key, sum, limit, path])
    neighbours(hd(path))
    |> Enum.filter(&(0 <= x(&1) && 0 <= y(&1) && isSpace(&1, key) && !Enum.member?(path, &1)))
    |> Enum.sort_by(&eval(&1, target))
    |> Enum.reduce(
      limit,
      fn (next, lastlimit) ->
        tmp = aSearch(target, key, sum + 1, lastlimit, [next] ++ path)

        if lastlimit == -1 or tmp < lastlimit do
          tmp
        else
          lastlimit
        end
      end
    )
  end

  # Task B: Breadth first iteration. 
  def count(key), do: count(key, 0, [{1,1}], [])

  def count(_, depth, nodes, visited)
  when 50 < depth or nodes == [], do: Enum.count(visited)

  def count(key, depth, nodes, visited) do

    selected = nodes
    |> Stream.flat_map(&(neighbours(&1)))
    |> Stream.filter(&(0 <= x(&1) && 0 <= y(&1)))
    |> Stream.filter(&(isSpace(&1, key)))
    |> Stream.uniq
    |> Stream.filter(&(!Enum.member?(visited, &1)))
    |> Enum.to_list

    visited = nodes ++ visited
    depth = depth + 1

    count(key, depth, selected, visited)
  end

  def solve(x, y, key) do
    IO.write(aSearch({x,y},key))
    IO.puts(" Steps")
    IO.write(count(key))
    IO.puts(" Tiles")
  end
end

IO.puts("Example\n")
Day13.solve(7,4,10)

IO.puts("\nTask:")
Day13.solve(31,39,1350)
