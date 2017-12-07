import React from 'react'
import {Card} from '@blueprintjs/core'
import styled from 'styled-components'
import {Link} from 'react-router-dom'
import Logo from '../images/cetacea_logo.png'

const SignUpCard = styled(Card)`
  margin: 20px 350px 20px 350px;
  height: 300px;
`
const LogoContainer = styled.img`
  margin-top: 15px;
  width: 60%;
  display: block;
  margin: auto;
`
const HelpText = styled.h4`
  margin-top: 30px;
  margin-bottom: 45px;
  text-align: center;
  font-family: 'Open Sans';
`

const GoogleButton = styled.div`
  .abcRioButton.abcRioButtonLightBlue { margin: 0 auto;}
  display: block;
  margin: auto;
`
const LogInText = styled.p`
  font-family: 'Open Sans';
  text-align: center;
  margin-top: 40px;
`

class SignupPage extends React.Component {

  componentDidMount() {
    gapi.signin2.render('g-signin2', {
      'scope': 'https://www.googleapis.com/auth/plus.login',
      'onsuccess': this.onSignIn.bind(this)
    });
  }

  onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile();
    var id_token = googleUser.getAuthResponse().id_token;

    var self = this;
    fetch('/api/login', {
      method: 'POST',
      body: JSON.stringify({
        idtoken: id_token,
        isSignup: "true"
      })
    }).then(function(resp){
      return resp.json();
    }).then(function(body){
      self.props.history.push("/", {userId:body.id});
    })
  }


  render () {
    return (
      <div>
        <SignUpCard>
          <Link to='/'>
            <LogoContainer src={Logo} />
          </Link>
          <HelpText>{"Sign up for Cetacea with your Google Account:"}</HelpText>
          <GoogleButton id="g-signin2" data-onsuccess={this.onSignIn}></GoogleButton>
          <Link to='/'>
            <LogInText><b>{"Already have an account? Log in here."}</b></LogInText>
          </Link>
        </SignUpCard>
      </div>
    )
  }
}

export default SignupPage
