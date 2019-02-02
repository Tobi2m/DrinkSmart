// load app server using express..
const express = require('express')
const app = express()
const morgan = require('morgan')
const mysql = require('mysql')
const crypto = require('crypto')
const uuid = require('uuid')
const bodyParser = require('body-parser')

app.use(morgan('combined'))
app.use(bodyParser.json()); // Accept JSON Params
app.use(bodyParser.urlencoded({extended: false})); // Accept URL Encoded params

//++++++++++++++++Connection properties++++++++++++++++

const connection=mysql.createConnection({
    host: 'us-cdbr-iron-east-01.cleardb.net',
        user: 'bbd883d66fc195',
        password: '6dde4363',
        database:'heroku_e045751d7ae8043'
})

//++++++++++++++++++Register/Login/Hashing+++++++++++++

//+++++++++++++++++++PASSWORD ULTIL++++++++++++++++++++
var genRandomString=function(length){
    return crypto.randomBytes(Math.ceil(length/2))
    .toString('hex') // convert to hexa format
    .slice(0,length); // return required number of characters
};

var sha512 = function (password,salt){
    var hash=crypto.createHmac('sha512',salt); // Use SHA512

    hash.update(password);
    var value=hash.digest('hex');
    return {
        salt:salt,
        passwordHash:value
    };
};

function saltHashPassword(userPassword){
    var salt=genRandomString(16); // Gen random string with 16 characters to salt

    var passwordData=sha512(userPassword,salt);
    return passwordData;
}

function checkHashPassword(userPassword,salt){
    var passwordData = sha512(userPassword,salt);
    return passwordData;
}


app.post('/register/',(req,res,next)=>{
    
    var post_data = req.body; // Get POST params

    var uid= uuid.v4(); // Get UUID v4
    var plaint_Password = post_data.password; // Get password from post params
    var hash_data = saltHashPassword(plaint_Password);
    var password = hash_data.passwordHash; // Get hash value
    var salt = hash_data.salt // Get salt

    var name = post_data.name;
    var email = post_data.email;

    connection.query('SELECT * FROM tab_benutzer WHERE email=?',[email],function(err,result,fields){
        connection.on('error',function(err){
            console.log('[MySQL ERROR]',err);
        });

        if(result && result.length)
            res.json('User already exists!!!');
        else
        {
            connection.query('INSERT INTO tab_benutzer (name,email,encrypted_password,salt) VALUES (?,?,?,?)',[name,email,password,salt],function(err,result,fields){
                connection.on('error',function(err){
                    console.log('[MySQL ERROR]',err);
                    res.json('Register error: ',err)
                });
                res.json('Register successful');
            })
        }
    });
})

app.post('/login/',(req,res,next)=>{
    
    var post_data = req.body;

    // Extract password and email from request
    var user_password = post_data.password;
    var email = post_data.email;

    connection.query('SELECT * FROM tab_benutzer WHERE email=?',[email],function(err,result,fields){
        connection.on('error',function(err){
            console.log('[MySQL ERROR]',err);
        });

        if(result && result.length){
            var salt = result[0].salt; // Get salt of result if account exists
            var encrypted_password = result[0].encrypted_password;
            // Hash password from Login request with salt in Database
            var hashed_password = checkHashPassword(user_password,salt).passwordHash;
            if(encrypted_password == hashed_password)
                res.end(JSON.stringify(result[0])) // If password is true, return all data of user
            else
                res.end(JSON.stringify('Login data incorrect!'))
        }
        else
        {
            res.json('User does not exist!!!');
        }
    });

})


//+++++++++++tab_benutzer+++++++++++++++++
/*
app.get('/register/:query',(req, res)=>{
    console.log("Register user with query: "+req.params.query)

    const connection = mysql.createConnection({
        host: 'us-cdbr-iron-east-01.cleardb.net',
        user: 'bbd883d66fc195',
        password: '6dde4363',
        database:'heroku_e045751d7ae8043'
    })
    
    var sqlquery="INSERT INTO tab_benutzer (Vorname,Nachname,Passwort,Email) Values "+req.params.query

    connection.query(sqlquery, (err, rows, fields) => {
        if(err){
            console.log("Failed to register user: "+err)
            res.sendStatus(500)
            return
        }

        console.log("I think we registered user successfully")
    })
    res.end();
    connection.end();

})

app.get('/login_request/:email',(req, res) => {
    console.log("Fetching user with email: " +req.params.email)

    const connection = mysql.createConnection({
        host: 'us-cdbr-iron-east-01.cleardb.net',
        user: 'bbd883d66fc195',
        password: '6dde4363',
        database:'heroku_e045751d7ae8043'
    })

    const email = req.params.email
    const queryString="SELECT * FROM tab_benutzer WHERE Email=?"
    connection.query(queryString, [email], (err, rows, fields) => {
        if(err){
            console.log("Failed to query for user: "+err)
            res.sendStatus(500)
            return
        }
        console.log("I think we fetched user successfully")

        res.json(rows)
        connection.end();
    })  
})
*/

// ++++++++tab_getraenk++++++++++++++++

app.get('/fetch_all_drinks',(req, res)=>{
    console.log("Fetching all drinks") 

    const queryString= "SELECT * FROM tab_getraenk"
    connection.query(queryString,(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for drinks: "+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
        connection.end();  
    })
})

app.get('/fetch_drink_by_id/:drinkid',(req, res)=>{
    console.log("Fetching drink with id:  "+req.params.drinkid)
    
    const drinkID=req.params.drinkid
    const queryString= "SELECT * FROM tab_getraenk WHERE GetraenkID=?"
    connection.query(queryString, [drinkID],(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for drinks: "+err)
            res.sendStatus(500)
            return
        }

        const drink = rows.map((row)=>{
            return {name: row.Bezeichnung, type: row.Sorte, alcohol: row.Alkohol}
        })
        res.json(drink)
        connection.end();
    })
})

app.get('/fetch_top10',(req, res)=>{
    console.log("Fetching Top 10")

    connection.query("SELECT * FROM tab_getraenk ORDER BY Aufrufe DESC LIMIT 10",(err,rows,fields)=>{
        if(err){
            console.log("Failed to fetch top10: "+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
    })
})

app.post('/update_views/',(req,res,next)=>{
    var post_data=req.body

    var drinkid=post_data.drinkid
    var sqlquery="UPDATE tab_getraenk SET Aufrufe=Aufrufe+1 WHERE GetraenkID="+drinkid

    connection.query(sqlquery,(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for drinks: "+err)
            res.sendStatus(500)
            return
        }
        res.json('UPDATE views successful!');
    });
})

app.post('/update_rating/',(req,res,next)=>{
    var post_data=req.body

    var drinkid = post_data.drinkid
    var rating = post_data.rating

    var sqlquery="UPDATE tab_getraenk SET Bewertungssumme=Bewertungssumme+"+rating+", Bewertungsanzahl=Bewertungsanzahl+1, Bewertung=Bewertungssumme/Bewertungsanzahl WHERE GetraenkID = "+drinkid

    connection.query(sqlquery,(err,rows,fields)=>{
        if(err){
            console.log("Failed to update rating count!: "+err)
            res.sendStatus(500)
            return
        }
        res.json('UPDATE rating count successful!');
    })
})
    
	/*
    const queryString= "SELECT Aufrufe FROM tab_getraenk WHERE GetraenkID=?"

    connection.query(queryString, [drinkID],(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for visits: "+err)
            res.sendStatus(500)
            return
        }

        const visits = rows.map((row)=>{
            return {visits: row.Aufrufe}
        })
        res.json(visits)
    })    
})
*/

//+++++++++++++tab_zutat++++++++++++++++++

app.get('/fetch_all_ingredients',(req, res)=>{
    console.log("Fetching all Ingredients") 

    const queryString= "SELECT * FROM tab_zutat"
    connection.query(queryString,(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for ingredients: "+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
        connection.end();
    })
})

//++++++++++++++tab_istzutatvon++++++++++++++++

app.get('/fetch_all_ingredient_drink_connections',(req, res)=>{
    console.log("Fetching all ingredient-drink connections") 

    const queryString= "SELECT * FROM tab_istzutatvon"
    connection.query(queryString,(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for ingredient-drink connections: "+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
        connection.end();
    })
})

//++++++++++++++tab_istfavoritvon++++++++++++++

app.get('/fetch_favourites_by_userid/:userid',(req, res)=>{
    console.log("Fetching favourites for User: "+req.params.userid) 

    const userid=req.params.userid
    const queryString= "SELECT * FROM tab_istfavoritvon WHERE BenutzerID=?"
    connection.query(queryString, [userid],(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for favourites: "+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
    })
})

app.post('/update_favourites/',(req, res,next)=>{
    var post_data=req.body

    var userid=post_data.userid
    var drinkid=post_data.drinkid
    //var sqlstring = 'INSERT INTO tab_istfavoritvon (BenutzerID, GetraenkID) VALUES (?,?)',[drinkid,userid]

    connection.query('INSERT INTO tab_istfavoritvon (BenutzerID, GetraenkID) VALUES (?,?)',[userid,drinkid],function(err,result,fields){
        console.log(drinkid);
        console.log(userid);
        connection.on('error',function(err){
            console.log('[MySQL ERROR]',err);
            res.json('INSERT favourite error: ',err)
        });
        res.json('INSERT favourite successful!');
    })
})

//+++++++++++++++tab_herkunftsland+++++++++++++

app.get('/fetch_all_countries',(req, res)=>{
    console.log("Fetching all source countries") 

    const userid=req.params.userid
    const queryString= "SELECT * FROM tab_herkunftsland"
    connection.query(queryString, [userid],(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for countries: "+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
    })
})

//+++++++++++++++++++++++++++++++++++++++++++++
app.get('')

app.get("/",(req,res) => {
    console.log("Responding to root route")
    res.send("Pöll du wappla  (responding to Route)")
})


const PORT = process.env.PORT || 3003
// localhost:PORT
app.listen(PORT, () => {
    console.log("Server is up and listening on : "+PORT)
})

