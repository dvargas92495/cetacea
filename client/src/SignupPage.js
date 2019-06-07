import React from 'react'
import {Card, Icon} from '@blueprintjs/core'
import {IconNames} from '@blueprintjs/icons'
import styled from 'styled-components'
import {Link} from 'react-router-dom'
import { onSignIn } from './actions';
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

const BackButton = styled(Link)`
  margin: 30px;
  font-size: 35px;
`
const BackButtonSpan = styled.span`
  .bp3-icon{
    font-size: 35px;
    margin-top: 20px;
  }
`

class SignupPage extends React.Component {

  componentDidMount() {
    gapi.signin2.render('g-signin2', {
      scope: 'https://www.googleapis.com/auth/userinfo.profile',
      onsuccess: onSignIn
    });
  }

  render () {
    return (
      <div>
        <BackButton to='/'>
          <BackButtonSpan><Icon icon={IconNames.CIRCLE_ARROW_LEFT} iconSize={35} /></BackButtonSpan>
        </BackButton>
        <SignUpCard>
          <Link to='/'>
            <LogoContainer src={Logo} />
          </Link>
          <HelpText>{"Sign up for Cetacea with your Google Account:"}</HelpText>
          <GoogleButton id="g-signin2"></GoogleButton>
          <Link to='/login'>
            <LogInText><b>{"Already have an account? Log in here."}</b></LogInText>
          </Link>
        </SignUpCard>
      </div>
    )
  }
}

export default SignupPage
