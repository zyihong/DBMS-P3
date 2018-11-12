// query 7: Find the city average friend count per user using MapReduce
// Using the same terminology in query6, we are asking you to write the mapper,
// reducer and finalizer to find the average friend count for each city.


var city_average_friendcount_mapper = function() {
  emit(this.hometown, this.friends.length)
};

var city_average_friendcount_reducer = function(key, values) {
  // implement the reduce function of average friend count
  return (values) => values.reduce((a, b) => a + b) / values.length;;
};

var city_average_friendcount_finalizer = function(key, reduceVal) {
  // We've implemented a simple forwarding finalize function. This implementation 
  // is naive: it just forwards the reduceVal to the output collection.
  // Feel free to change it if needed.
  var ret = reduceVal;
  return ret;
}
