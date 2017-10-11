import React from 'react'
import {EditableText} from '@blueprintjs/core'
import Button from './components/Button.js'
import NavBar from './components/NavBar.js'
import moment from 'moment';

class NewComponent extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      textContent : "",
      date: moment().format("LL")
    }
    this.onChange = this.onChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  onChange (newText: string) {
    return this.setState({textContent: newText})
  }

  handleSubmit(event) {
    alert('A name was submitted: ');
    event.preventDefault();
  }

  // render () {
  //   return (
  //     <form onSubmit={this.handleSubmit}>
  //       <EditableText
  //           multiline
  //           maxLines={12}
  //           minLines={3}
  //           multiline={true}
  //           placeholder="Try Me!"
  //           value={this.state.textContent}
  //           onChange={this.onChange}
  //       />
  //     </form>
  //   )
  // }

  render () {
    return (
      <div>
        <NavBar />
          <div >
            {this.state.date.toString()}
          </div>
        <form onSubmit={this.handleSubmit}>
          <EditableText
              multiline
              maxLines={12}
              minLines={3}
              multiline={true}
              placeholder="Try Me!"
              value={this.state.textContent}
              onChange={this.onChange}
          />
        </form>
      </div>
    )
  }
}


export default NewComponent
