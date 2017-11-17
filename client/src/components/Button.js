import React from 'react'
import styled from 'styled-components'

const Attempt = styled.button`
  width: 100px;
  height: 40px;
  color: #757575;
  font-family: 'Allerta';
  font-size: 12px;
  background: #FFFFFF;
  border: 1px solid #757575;
  border-radius: 10px;
  display: inline-block;
  &:hover {
    cursor: pointer;
    cursor: hand;
  }`

class Button extends React.Component {
  render () {
    return (
      <Attempt onClick={this.props.press}>{this.props.text}</Attempt>
    )
  }
}

Button.defaultProps = {
  text: 'Press me!',
  press: null
}

export default Button
