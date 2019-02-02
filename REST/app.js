// load app server using express..
var express = require('express')
var app = express()
const morgan = require('morgan')
var mysql = require('mysql')
var crypto = require('crypto')
var uuid = require('uuid')
var bodyParser = require('body-parser')

app.use(morgan('combined'))

var con=mysql.createConnection({
    host: 'us-cdbr-iron-east-01.cleardb.net',
        user: 'bbd883d66fc195',
        password: '6dde4363',
        database:'heroku_e045751d7ae8043'
})

//PASSWORD ULTIL
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

app.use(bodyParser.json()); // Accept JSON Params
app.use(bodyParser.urlencoded({extended: true})); // Accept URL Encoded params


app.get("/tut",(req,res,next)=>{
    console.log('Password: 123456');
    var encrypt=saltHashPassword("123456")
    console.log('Encrypt: '+encrypt.passwordHash);
    console.log('Salt:  '+encrypt.salt);
})


//+++++++++++tab_benutzer++++++++++++++++++ 
app.post('/regist/',(req,res,next)=>{
    
    var post_data = req.body; // Get POST params

    //var uid= uuid.v4(); // Get UUID v4
    var plaint_Password = post_data.password; // Get password from post params
    var hash_data = saltHashPassword(plaint_Password);
    var password = hash_data.passwordHash; // Get hash value
    var salt = hash_data.salt // Get salt

    var name = post_data.name;
    var email = post_data.email;

    con.query('SELECT * FROM tab_benutzer WHERE email=?',[email],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]',err);
        });

        if(result && result.length)
            res.json('User already exists!!!');
        else
        {
            con.query('INSERT INTO tab_benutzer (name,email,encrypted_password,salt) VALUES (?,?,?,?)',[name,email,password,salt],function(err,result,fields){
                con.on('error',function(err){
                    console.log('[MySQL ERROR]',err);
                    res.json('Register error: ',err)
                });
                res.json('Register successful');
            })
        }
    });
    
})

// ++++++++tab_getraenk+++++++++++++++

app.get('/fetch_all_drinks',(req, res)=>{
    console.log("Fetching all drinks") 

    const queryString= "SELECT * FROM tab_getraenk"
    con.query(queryString,(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for drinks: "+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
        con.end();  
    })
})

app.get('/fetch_drink_by_id/:drinkid',(req, res)=>{
    console.log("Fetching drink with id:  "+req.params.drinkid)
    
    const drinkID=req.params.drinkid
    const queryString= "SELECT * FROM tab_getraenk WHERE GetraenkID=?"
    con.query(queryString, [drinkID],(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for drinks: "+err)
            res.sendStatus(500)
            return
        }

        const drink = rows.map((row)=>{
            return {name: row.Bezeichnung, type: row.Sorte, alcohol: row.Alkohol}
        })
        res.json(drink)
        con.end();
    })
})

app.get('/fetch_top10',(req, res)=>{
    console.log("Fetching Top 10")

    con.query("SELECT * FROM tab_getraenk ORDER BY Aufrufe DESC LIMIT 10",(err,rows,fields)=>{
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

    con.query(sqlquery,(err,rows,fields)=>{
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

    con.query(sqlquery,(err,rows,fields)=>{
        if(err){
            console.log("Failed to update rating count!: "+err)
            res.sendStatus(500)
            return
        }
        res.json('UPDATE rating count successful!');
    })
})

/*
app.get('/update_drink_visits/:drinkid',(req, res)=>{
    console.log("Update drink with id:  "+req.params.drinkid)
    
    const drinkID=req.params.drinkid    

    var sqlquery="UPDATE tab_getraenk SET Aufrufe=Aufrufe+1 WHERE GetraenkID="+drinkID

    connection.query(sqlquery, (err, rows, fields) => {
        if(err){
            console.log("Failed to update visits: "+err)
            console.log(sqlquery)
            res.sendStatus(500)
            return
        }
        console.log("I think we updated visits successfully")
    })
    
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
    con.query(queryString,(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for ingredients: "+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
        con.end();
    })
})

//++++++++++++++tab_istzutatvon++++++++++++++++

app.get('/fetch_all_ingredient_drink_connections',(req, res)=>{
    console.log("Fetching all ingredient-drink connections") 

    const queryString= "SELECT * FROM tab_istzutatvon"
    con.query(queryString,(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for ingredient-drink connections: "+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
        con.end();
    })
})

//++++++++++++++tab_istfavoritvon++++++++++++++

app.post('/update_favourites/',(req, res,next)=>{
    var post_data=req.body

    var userid=post_data.userid
    var drinkid=post_data.drinkid
    //var sqlstring = 'INSERT INTO tab_istfavoritvon (BenutzerID, GetraenkID) VALUES (?,?)',[drinkid,userid]

    con.query('INSERT INTO tab_istfavoritvon (BenutzerID, GetraenkID) VALUES (?,?)',[userid,drinkid],function(err,result,fields){
        console.log(drinkid);
        console.log(userid);
        con.on('error',function(err){
            console.log('[MySQL ERROR]',err);
            res.json('INSERT favourite error: ',err)
        });
        res.json('INSERT favourite successful!');
    })
})

app.get('/fetch_favourites_by_userid/:userid',(req, res)=>{
    console.log("Fetching favourites for User: "+req.params.userid) 

    const userid=req.params.userid
    const queryString= "SELECT * FROM tab_istfavoritvon WHERE BenutzerID=?"
    con.query(queryString, [userid],(err,rows,fields)=>{
        if(err){
            console.log("Failed to query for favourites: "+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
    })
})
/*
app.get('/insert_favourite/:query',(req, res)=>{
    console.log("Insert favourite with query: "+req.params.query)
    
    var sqlquery="INSERT INTO tab_istfavoritvon (BenutzerID, GetraenkID) VALUES "+req.params.query

    connection.query(sqlquery, (err, rows, fields) => {
        if(err){
            console.log("Failed to register user: "+err)
            res.sendStatus(500)
            return
        }

        console.log("I think we inserted favourites successfully")
    })
    res.end();
    connection.end();
})
*/
//+++++++++++++++tab_herkunftsland+++++++++++++

app.get('/fetch_all_countries',(req, res)=>{
    console.log("Fetching all source countries") 

    const userid=req.params.userid
    const queryString= "SELECT * FROM tab_herkunftsland"
    con.query(queryString, [userid],(err,rows,fields)=>{
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

