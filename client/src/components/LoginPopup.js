import React from 'react'
import Button from './Button.js'
import GoogleButton from './GoogleButton'
import styled from 'styled-components'
import {Dialog} from '@blueprintjs/core'

const PopupText = styled.h2`
  display: block;
  text-align: center;
  font-size: 1.2em;
  margin-top: 30px;
  font-family: Allerta;
  color: #616161;
  font-weight: lighter;
  margin-bottom: 30px;`
const LoginDialog = styled(Dialog)`
  background: #FFFFFF;
  .bp3-dialog-header {
    background: #D4EDF0;
  }`

class LoginPopup extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      id: null
    }
  }

  render () {
    return (
      <div>
        <LoginDialog isOpen={this.props.isOpen} onClose={this.props.onClose} title=''>
          <div>
            <PopupText>
              {"Please login with your Google account below."}
            </PopupText>

            <GoogleButton id="g-signin2" login={this.props.login}></GoogleButton>
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
