// find the oldest friend for each user who has a friend. 
// For simplicity, use only year of birth to determine age, if there is a tie, use the one with smallest user_id
// return a javascript object : key is the user_id and the value is the oldest_friend id
// You may find query 2 and query 3 helpful. You can create selections if you want. Do not modify users collection.
//
//You should return something like this:(order does not matter)
//{user1:userx1, user2:userx2, user3:userx3,...}

function oldest_friend(dbname){
  db = db.getSiblingDB(dbname);
  var results = {};
  var friend = {};
  // TODO: implement oldest friends
  db.users.find({}).forEach((user) => {
    user.friends.forEach((fri) => {
      if (fri in friend) {
        friend[fri].add(user.user_id);
      }
      else {
        friend[fri] = new Set();
      }
      if (user.user_id in friend) {
        friend[user.user_id].add(fri);
      }
      else {
        friend[user.user_id] = new Set();
      }
    });
  });
  for (var key of Object.keys(friend)) {
    friend[key] = db.users.find({
      user_id: {
        $in: Array.from(friend[key])
      }
      }, {user_id: 1, YOB: 1
    }).sort({YOB: 1, user_id:1}).limit(1);
  }
  for (var key of Object.keys(friend)) {
    if (friend[key].hasNext()) {
      var found = friend[key].next();
      results[key] = found.user_id;
    }
  }
  // return an javascript object described above
  return results
}
