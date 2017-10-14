import React from 'react'
import styled from 'styled-components'
import {Link} from 'react-router-dom'

class NavBar extends React.Component {

  render () {
    const NavBar = styled.ul`
      list-style-type: none;
      margin: 0;
      padding: 0;
      overflow: hidden;
      background-color: #D4EDF0;
      height: 40px;
      font-family: Allerta;
      border: 1px solid #616161;
      border-radius: 0px;
    `;

    const List = styled.li`
      float: right;
    `

    const Page = styled(Link)`
      display: block;
      font-family: Allerta;
      color: #616161;
      text-align: center;
      padding: 10px 40px;
      text-decoration: none;
      &:hover {
        color: #3F51B5;
        text-decoration: none;
      }
    `
    console.log(this.props.pages)
    return (
      <div>
        <NavBar>
          {this.props.pages.map(page => <List><Page to={page[1]}>{page[0]}</Page></List>)}
        </NavBar>
      </div>
    )
  }
}

NavBar.defaultProps = {
  pages: [['Home', '/']]
}

export default NavBar

// {this.props.pages.map(page => <List><Link to={page[1]}>{page[0]}</Link></List>)}
