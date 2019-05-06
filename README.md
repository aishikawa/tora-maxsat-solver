# tora-maxsat-solver
This is incomplete Max-Sat solver. 
This solver can handle DIMACS CNF format instances.
This solver outputs unsatisfied value.

## Usage
### Build
Use ant.
```shell
$ ant
```
This comand makes `maxsat.jar` in dest directory.

### Run
```shell
$ java -jar dest/maxsat.jar -instance foo.cnf -algorithm mls
```
  
## Algorithms
5 algorithms are implemented.
- mls (Multi-Start Local Search)
- ils (Iterated Local Search)
- gls (Genetic Local Search)
- ts (Tabu Search)
- sa (Simulated Annealing)

## LICENSE
 Apache License Version 2.0