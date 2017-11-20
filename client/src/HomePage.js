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
  display: inline-block;`;

class HomePage extends React.Component {

  constructor(props) {
    super(props);
    this.loggedOutPages = [ ['about', '/about'], ['log in', this.openDialog.bind(this)]];
    this.loggedInPages = [ ['about', '/about'], ['log out', '/journal'], ['journal', '/journal'], ['groups', '/group']];
    this.state = {
      isOpen: false,
      pages: this.loggedOutPages,
      userId: 0
    };
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
