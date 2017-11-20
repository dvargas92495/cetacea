import React from 'react'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import styled from 'styled-components'
import {Link} from 'react-router-dom'
import LoginPopup from './components/LoginPopup.js'

const Title = styled.h1`
  display: block;
  text-align: center;
  font-size: 4em;
  margin-top: 1.5em;
  margin-bottom: 0.5em;
  margin-left: 0;
  margin-right: 0;
  font-family: Allerta;
  color: #FFFFFF;
  font-weight: bold;`;
const Subtitle = styled.h2`
  display: block;
  text-align: center;
  font-size: 2em;
  margin-top: 0.5em;
  margin-bottom: 0.5em;
  margin-left: 0;
  margin-right: 0;
  font-family: Allerta;
  color: #FFFFFF;
  font-weight: normal;`;
const Space = styled.div`
  margin-right: 50px;
  display: inline-block;`;

class HomePage extends React.Component {

  constructor(props) {
    super(props);
    this.loggedOutPages = [ ['About', '/about'], ['Log In', this.openDialog.bind(this)]];
    this.loggedInPages = [ ['About', '/about'], ['Log Out', '/journal'], ['Groups', '/group'], ['Journal', '/journal']];
    this.state = {
      isOpen: false,
      pages: this.loggedOutPages,
      userId: 0
    };

    var self = this;
    gapi.load('auth2', function(){
      var a2 = gapi.auth2.init({
        client_id: '548992550759-kmikahq1pkfhffgps85151j5o2a6gduu.apps.googleusercontent.com',
        scope: 'https://www.googleapis.com/auth/plus.login'
      });
      a2.then(function(){
        if(a2.isSignedIn.get()) {
          var id_token = a2.currentUser.get().getAuthResponse().id_token;
          fetch('/api/login', {
            method: 'POST',
            body: JSON.stringify({
              idtoken: id_token,
              isSignup: "false"
            })
          }).then(function(resp){
            return resp.json();
          }).then(function(body){
            self.setState({id: body.id});
            self.updateUser(body.id);
          });
        }
      })
    });
  }

  openDialog() {
    this.setState({isOpen: true })
  }

  closeDialog() {
    this.setState({isOpen: false })
  }

  updateUser(id){
    this.setState({'pages': this.loggedInPages});
    this.setState({'userId': id});
  }

  render () {
    return (
      <div>
        <NavBar pages={this.state.pages} userId={this.state.userId}/>
        <LoginPopup isOpen={this.state.isOpen} onClose={this.closeDialog.bind(this)} updateUser={this.updateUser.bind(this)}/>
        <Title>
          welcome to cetacea
        </Title>
        <Subtitle>
          journaling for a porpoise
        </Subtitle>
        <div style={{'textAlign': "center", 'topMargin': '25px'}}>
          {this.state.userId == 0 ?
            <Link to='/signup'>
              <Button text="sign up"/>
            </Link> :
            <Link to={"/journal/"+this.state.userId}>
              <Button text="let's go"/>
            </Link>
          }
          <Space/>
          <Link to = '/about'>
            <Button text="learn more" />
          </Link>
        </div>
      </div>
    )
  }
}

export default HomePage
// <div id="g-signin2" data-onsuccess={this.onSignIn}></div>
