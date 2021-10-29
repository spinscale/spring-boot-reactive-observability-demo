step('launch app', async () => {
  await page.goto('https://xeraa.wtf');
});

step('assert table', async () => {
  const table = await page.$('table');
  // table  titles
  expect(await table.textContent()).toContain('Image');
  expect(await table.textContent()).toContain('Name');
  expect(await table.textContent()).toContain('Description');
  expect(await table.textContent()).toContain('Tags');
  // concrete product name
  expect(await table.textContent()).toContain('My Product');
});
