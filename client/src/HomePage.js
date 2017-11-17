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
  font-weight: bold;`
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
  font-weight: normal;`
const Space = styled.div`
  margin-right: 50px;
  display: inline-block;`

class HomePage extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      isOpen: false,
      pages: [ ['about', '/about'], ['log in', this.toggleDialog.bind(this)]],
      userId: 0
    }
    this.toggleDialog = this.toggleDialog.bind(this)

  }

  toggleDialog() {
    this.setState({isOpen: !this.state.isOpen })
  }

  // componentDidMount() {
  //   gapi.signin2.render('g-signin2', {
  //     'scope': 'https://www.googleapis.com/auth/plus.login',
  //     'onsuccess': this.onSignIn
  //   })
  // }

  // onSignIn(googleUser) {
  //   var profile = googleUser.getBasicProfile()
  //   var id_token = googleUser.getAuthResponse().id_token
  //
  //
  //   fetch('/api/login', {
  //     method: 'POST',
  //     body: JSON.stringify({
  //       idtoken: id_token,
  //       isSignup: "false"
  //     })
  //   }).then(function(resp){
  //     return resp.json();
  //   }).then(function(body){
  //     console.log(body)
  //   })
  //
  //   console.log('Name: ' + profile.getName());
  // }


  render () {
    return (
      <div>
        <NavBar pages={this.state.pages} />
        <LoginPopup isOpen={this.state.isOpen} onClose={this.toggleDialog} />
        <Title>
          welcome to cetacea
        </Title>
        <Subtitle>
          journaling for a porpoise
        </Subtitle>
        <div style={{'textAlign': "center", 'topMargin': '25px'}}>
          <Link to='/signup'>
            <Button text="sign up" />
          </Link>
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
