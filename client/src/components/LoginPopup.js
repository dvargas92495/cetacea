import React from 'react'
import Button from './Button.js'
import styled from 'styled-components'
import {Dialog} from '@blueprintjs/core'

const PopupText = styled.h2`
  display: block;
  text-align: center;
  font-size: 1.2em;
  margin-top: 1em;
  font-family: Allerta;
  color: #616161;
  font-weight: lighter;`
const LoginDialog = styled(Dialog)`
  background: #FFFFFF;
  .pt-dialog-header {
    background: #D4EDF0;
  }`

class LoginPopup extends React.Component {
  constructor(props) {
    super(props)
    this.onSignIn = this.onSignIn.bind(this)
  }

  componentDidUpdate() {
    if (this.props.isOpen == true) {
      gapi.signin2.render('g-signin2', {
            'scope': 'https://www.googleapis.com/auth/plus.login',
            'onsuccess': this.onSignIn
      })
    }
  }

  onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile()
    var id_token = googleUser.getAuthResponse().id_token

    var self = this
    fetch('/api/login', {
      method: 'POST',
      body: JSON.stringify({
        idtoken: id_token,
        isSignup: "false"
      })
    }).then(function(resp){
      return resp.json();
    }).then(function(body){
      self.setState({id: body.id})
    })

    console.log('Name: ' + profile.getName());
  }

  render () {
    return (
      <div>
        <LoginDialog isOpen={this.props.isOpen} onClose={this.props.onClose} title=''>
          <div>
            <PopupText>
              {"Please login with your Google account below."}
            </PopupText>

            <div id="g-signin2" data-onsuccess={this.onSignIn}></div>
          </div>
        </LoginDialog>
      </div>
    )
  }
}

LoginPopup.defaultProps = {
  isOpen: false,
  onClose: null
}

export default LoginPopup
