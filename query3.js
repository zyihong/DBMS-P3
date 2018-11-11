//query3
//create a collection "cities" to store every user that lives in every city
//Each document(city) has following schema:
/*
{
  _id: city
  users:[userids]
}
*/

function cities_table(dbname) {
    db = db.getSiblingDB(dbname);
    // TODO: implemente cities collection here
    cities = {};
    db.users.find({}, {user_id: 1, "hometown.city": 1}).forEach((ele) => {
        if (ele.hometown.city) {
            if (ele.hometown.city in cities) {
                cities[ele.hometown.city].push(ele.user_id);
            }
            else {
                cities[ele.hometown.city] = [ele.user_id];
            }
        }
    });
    for (var key of Object.keys(cities)) {
        // print(key);
        // print(cities[key]);
        db.cities.insert({_id: key, users: cities[key]});
    }
    // Returns nothing. Instead, it creates a collection inside the datbase.

}
