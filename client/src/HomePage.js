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
const ButtonStyling = styled.div`
  text-align: center;
  margin-top: 30px;
`

class HomePage extends React.Component {

  constructor(props) {
    super(props)
    const userId = props.location.state ? props.location.state.userId : 0
    this.state = {
      userId: userId
    }
  }

  redirect(){ //lol this is so bad, we really need our BasicPage refactor
    this.setState({userId: 0})
  }

  render () {
    return (
      <div>
        <NavBar redirect={this.redirect.bind(this)} onlogin={this.setState.bind(this)} userId={this.state.userId}/>
        <Title>
          welcome to cetacea
        </Title>
        <Subtitle>
          porpoiseful journaling
        </Subtitle>
        <ButtonStyling>
          {this.state.userId == 0 ?
            <Link to='/signup'>
              <Button text="sign up"/>
            </Link> :
            <Link to={{pathname: "/journal", state:{userId: this.state.userId}}}>
              <Button text="let's go"/>
            </Link>
          }
          <Space/>
          <Link to = {{pathname: "/about", state:{userId: this.state.userId}}}>
            <Button text="learn more" />
          </Link>
        </ButtonStyling>
      </div>
    )
  }
}

export default HomePage
