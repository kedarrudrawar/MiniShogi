import axios from 'axios';

export async function post(url, body){
    await axios.post(url, body, 
        {
            headers: { 'Content-Type': 'application/json' }
        }).then((response ) => {
            console.log('RESPOSNE');
            console.log(response.text());

        }).catch((err) => {
            console.log(err);
        });
}

export async function get(url){
    await axios.get(url,
        { 
            headers: { 'Content-Type' : 'application/json' }
        }).then((response) => {
            console.log('returning:');
            console.log(response.data);
            return response.data;
        }).catch((err) => {
            console.log(err);
        })
}


export async function handleRequest(url, method='GET', body=null){
    let data = null;
    let requestOptions = {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: body,
    };
    console.log(body);
    console.log(requestOptions)


    await fetch(url, requestOptions)
        .then((response) => response.text())
        .then(text => {
            try {
                data = JSON.parse(text);
                return data;
            } catch (err){
                console.log(err);
            }
        })
        .catch((err) => {
            console.log("Caught exception.");
            console.log(err);
        });
    return data;
}
