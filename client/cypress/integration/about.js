describe("About Page", () => {
  it("Navigates to the page as a guest through 'learn more' Button", () => {
    cy.visit('/');
    cy.contains('learn more').click();
    cy.url().should('include', '/about');
    cy.contains('About Cetacea');
  });
  it("Navigates back home as a guest through logo", () => {
    cy.visit('/about');
    cy.get('img').click();
    cy.url().should('include', '/');
    cy.contains('welcome to cetacea');
    cy.contains('porpoiseful journaling');
  });
});