import React from 'react'
import {render} from 'react-dom'
import styled from 'styled-components'

import NewComponent from './NewComponent.js'
import HomePage from './HomePage.js'
import JournalPage from './JournalPage.js'

class WelcomePage extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      activePage : 0
    }
  }

  handlePageClick() {
    console.log('hi')
    if (this.state.activePage == 0){
      this.setState({activePage: 1})
    }
    else {
      this.setState({activePage: 0})

    }
  }

  fakeFunct() {
    if (this.state.activePage == 0){
      return (
        <div>
          <HomePage onClick={()=>this.handlePageClick()} />
        </div>
      )
    }
    else {
      return (
        <div>
          <JournalPage onClick={() => this.handlePageClick()}/>
        </div>
      )
    }
  }

  render () {
    return this.fakeFunct()
  }
}

render(<WelcomePage/>, document.getElementById('welcome'));
