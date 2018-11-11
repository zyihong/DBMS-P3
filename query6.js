// query6 : Find the Average friend count per user for users
//
// Return a decimal variable as the average user friend count of all users
// in the users document.

function find_average_friendcount(dbname){
  db = db.getSiblingDB(dbname)
  // TODO: return a decimal number of average friend count
  var A = db.users.aggregate([{$project: {"_id": 0, "user_id": 1, "numOfFriends": {$size: "$friends"}}}]);
  var total = 0;
  var sum = 0;
  A.forEach(function(UA){
    sum += UA.numOfFriends;
    total += 1;
  })

  return sum/total;
}
