import React, { useEffect } from 'react'
import styled from 'styled-components'

const GoogleButtonContainer = styled.div`
  .abcRioButton.abcRioButtonLightBlue { margin: 0 auto;}
  display: block;
  margin: auto;
  margin-bottom: 30px;
`

const GoogleButton = ({ login }) => {
  useEffect(() => {
    gapi.signin2.render('g-signin2', {
      'scope': 'https://www.googleapis.com/auth/userinfo.email',
      'onsuccess': login
    });
  }, [login]);
  return (
    <GoogleButtonContainer id='g-signin2'/>
  )
}

export default GoogleButton;