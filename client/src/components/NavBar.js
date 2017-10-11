import React from 'react'
import styled from 'styled-components'

class NavBar extends React.Component {

  // displayNavPages () {
  //   this.props.pages.reverse()
  //   for (page in this.props.pages) {
  //
  //     <List><Link href={page[1]}>
  //
  //   }
  //   <List><Link href="#">Help</Link></List>
  //   <List><Link href="#">Settings</Link></List>
  //   <List><Link href="#">Groups</Link></List>
  //   <List><Link href="#">Journal</Link></List>
  // }

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

    const Link = styled.a`
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
    return (
      <div>
        <NavBar>
          {this.props.pages.map(page => <List><Link key={page[0]} href={page[1]}>{page[0]}</Link></List>)}
        </NavBar>
      </div>
    )
  }
}

NavBar.defaultProps = {
  pages: [['Home', 'www.cetacea.xyz']]
}

export default NavBar
