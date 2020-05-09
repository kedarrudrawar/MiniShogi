
export async function handleRequest(url){
    let data = null;
    await fetch(url)
        .then((response) => response.text())
        .then(text => {
            try {
                data = JSON.parse(text);
                return data;
            } catch (err){
                console.log(err);
            }
        })
        .catch(() => {
            console.log("Caught exception.");
        });
    return data;
}