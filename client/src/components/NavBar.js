import React from 'react'
import styled from 'styled-components'
import {Link} from 'react-router-dom'

const Bar = styled.ul`
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #D4EDF0;
  height: 40px;
  font-family: Allerta;
  border: 1px solid #616161;
  border-radius: 0px;`
const List = styled.li`
  float: right;
  display: block;
  font-family: Allerta;
  color: #616161;
  text-align: center;
  padding: 10px 40px;
  text-decoration: none;
  &:hover {
    color: #3F51B5;
    text-decoration: none;
    cursor: pointer;
    cursor: hand;
  }`
const Page = styled(Link)`
  display: block;
  font-family: Allerta;
  color: #616161;
  text-align: center;
  text-decoration: none;
  &:hover {
    color: #3F51B5;
    text-decoration: none;
  }`

class NavBar extends React.Component {

  renderList () {
    return(
      <div>
        {this.props.pages.map(function(listValue){
          if (typeof(listValue[1]) === 'string') {
            return (
              <List key={listValue[0]}>
                <Page key={listValue[0]} to={listValue[1]}>{listValue[0]}</Page>
              </List>
            )
          }
          else {
            return <List key={listValue[0]} onClick={listValue[1]}>{listValue[0]}</List>
          }
        })}
      </div>
    )
  }

  render () {
    return (
      <div>
        <Bar>
          {this.renderList()}
        </Bar>
      </div>
    )
  }
}

NavBar.defaultProps = {
  pages: [['Home', '/']]
}

export default NavBar
// {this.props.pages.map(page => <List key={page[0]}><Page key={page[0]} to={page[1]}>{page[0]}</Page></List>)}
