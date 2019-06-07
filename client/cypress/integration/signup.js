import { onSignIn } from '../../src/actions';

const mockGoogleUser = {
  getAuthResponse: () => ({
    id_token: "MOCK_ID_TOKEN"
  })
};

describe("Sign up", () => {
  it("Signs user up and logs them in", () => {
    cy.visit('/', {
      onBeforeLoad(win) {
        cy.stub(win, 'open', () => onSignIn(mockGoogleUser));
      }
    });
    cy.contains('sign up').click();
    cy.contains('Sign up for Cetacea with your Google Account');
    cy.contains('Sign in').click().wait(5000);
    cy.contains('Journal');
  });
});
