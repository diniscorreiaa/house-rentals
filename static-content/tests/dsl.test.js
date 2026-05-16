import {element} from "../sparouter/dsl-functions.js"

function checkProps(elem, propsObj) {
    const propNames = []
    elem.getAttributeNames().forEach(propName => propNames.push(propName))

    const expectedProps = {}
    propNames.forEach(propName => {
        expectedProps[propName.toLowerCase()] = elem.getAttribute(propName)
    })

    propsObj.should.deep.equal(expectedProps)
}

describe('element()', () => {
    it("creates empty element", () => {
        const result = element("div")

        result.tagName.should.be.equal("DIV")
        result.textContent.should.be.equal("")
        result.childNodes.should.be.empty
        result.getAttributeNames().should.be.empty
    })

    it("creates element with just text", () => {
        const exampleText = "I'm a string!"
        const result = element("p", exampleText)

        result.tagName.should.be.equal("P")
        result.textContent.should.be.equal(exampleText)
        result.childNodes.should.be.have.lengthOf(1)
        result.getAttributeNames().should.be.empty
    })

    it("creates element with just other nested elements", () => {
        const result = element("ul", [element("li"), element("li")])

        result.tagName.should.be.equal("UL")
        result.textContent.should.be.equal("")
        result.childNodes.should.be.have.lengthOf(2)
        result.getAttributeNames().should.be.empty
    })

    it("creates element with just properties", () => {
        const attributes = {type: "button", id: "someId"}
        const result = element("input", attributes)

        result.tagName.should.be.equal("INPUT")
        result.textContent.should.be.equal("")
        result.childNodes.should.be.empty
        checkProps(result, attributes)
    })

    it("creates element with text, nested elements, and properties", () => {
        const exampleText = "braaaaa"
        const props = {id: "main"}
        const result = element("div", exampleText, [element("p")], props)

        result.tagName.should.be.equal("DIV")
        result.textContent.should.be.equal(exampleText)
        result.childNodes.should.be.lengthOf(2)
        checkProps(result, props)
    })
})
