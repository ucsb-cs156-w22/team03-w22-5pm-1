import { fireEvent, render, waitFor } from "@testing-library/react";
import { earthquakeFixtures } from "fixtures/earthquakeFixtures";
import EarthquakesTable from "main/components/Earthquakes/EarthquakesTable"
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import { currentUserFixtures } from "fixtures/currentUserFixtures";


describe("EarthquakesTable tests", () => {
  const queryClient = new QueryClient();

  test("renders without crashing for empty table with user not logged in", () => {
    const currentUser = null;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <EarthquakesTable Features={[]} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );
  });

  test("renders without crashing for empty table for ordinary user", () => {
    const currentUser = currentUserFixtures.userOnly;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <EarthquakesTable Features={[]} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );
  });

  test("renders without crashing for empty table for admin", () => {
    const currentUser = currentUserFixtures.adminUser;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <EarthquakesTable Features={[]} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );
  });

  test("Has the expected colum headers and content for adminUser", () => {

    const currentUser = currentUserFixtures.adminUser;

    const { getByText, getByTestId } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <EarthquakesTable Features={earthquakeFixtures.twoEarthquakes} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>

    );

    const expectedHeaders = ["Title", "Magnitude", "Place", "Time"];
    const expectedFields = ["title", "mag", "place", "time"];
    const testId = "EarthquakesTable";

    expectedHeaders.forEach( (headerText) => {
      const header = getByText(headerText);
      expect(header).toBeInTheDocument();
    } );

    expectedFields.forEach((field) => {
      const header = getByTestId(`${testId}-cell-row-0-col-${field}`);
      expect(header).toBeInTheDocument();
    });

    expect(getByTestId(`${testId}-cell-row-0-col-title`)).toHaveTextContent("M 6.9 - 10km ESE of Ojai, CA");
    expect(getByTestId(`${testId}-cell-row-1-col-title`)).toHaveTextContent("M 2.8 - 9kmN of San Juan Bautista, CA");
    expect(getByTestId(`${testId}-cell-row-0-col-mag`)).toHaveTextContent(6.9);
    expect(getByTestId(`${testId}-cell-row-1-col-mag`)).toHaveTextContent(2.8);
    expect(getByTestId(`${testId}-cell-row-0-col-place`)).toHaveTextContent("Ojai, CA");
    expect(getByTestId(`${testId}-cell-row-1-col-place`)).toHaveTextContent("San Juan Bautista");
    expect(getByTestId(`${testId}-cell-row-0-col-time`)).toHaveTextContent(1644571919000);
    expect(getByTestId(`${testId}-cell-row-1-col-time`)).toHaveTextContent(1644571919000);

  });

});

