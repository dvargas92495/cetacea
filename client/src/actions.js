import history from './history';

export const onSignIn = googleUser => {
  var id_token = googleUser.getAuthResponse().id_token;

  var self = this;
  return fetch('/api/login', {
    method: 'POST',
    body: JSON.stringify({
      idtoken: id_token
    })
  }).then(function(resp){
    return resp.json();
  }).then(function(body){
    history.push("/", {userId:body.id});
  }).catch(e => {
    console.log(e.message);
  });
}