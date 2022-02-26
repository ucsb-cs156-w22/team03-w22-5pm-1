import { render, waitFor, fireEvent } from "@testing-library/react";
import EarthquakeCreatePage from "main/pages/Earthquakes/EarthquakeCreatePage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";

import { apiCurrentUserFixtures }  from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";
import { earthquakeFixtures } from "fixtures/earthquakeFixtures";

const mockToast = jest.fn();
jest.mock('react-toastify', () => {
    const originalModule = jest.requireActual('react-toastify');
    return {
        __esModule: true,
        ...originalModule,
        toast: (x) => mockToast(x)
    };
});

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => {
    const originalModule = jest.requireActual('react-router-dom');
    return {
        __esModule: true,
        ...originalModule,
        Navigate: (x) => { mockNavigate(x); return null; }
    };
});


describe("EarthquakesCreatePage tests", () => {

    const axiosMock =new AxiosMockAdapter(axios);
    
    beforeEach(() => {
        axiosMock.reset();
        axiosMock.resetHistory();
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
    });

    test("renders without crashing", () => {
        const queryClient = new QueryClient();
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <EarthquakeCreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        );
    });

    test("when you fill in the form and hit submit, it makes a request to the backend with one test", async () => {
        const queryClient = new QueryClient();

        axiosMock.onPost("/api/earthquakes/retrieve").reply( 202, earthquakeFixtures.oneEarthquakeResponse );

        const { getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <EarthquakeCreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => {
            expect(getByTestId("EarthquakesForm-minMag")).toBeInTheDocument();
        });

        const minMagField = getByTestId("EarthquakesForm-minMag");
        const distField = getByTestId("EarthquakesForm-dist");
        const submitButton = getByTestId("EarthquakesForm-submit");


        fireEvent.change(minMagField, { target: { value: '5.8' } });
        fireEvent.change(distField, { target: { value: '100' } });

        expect(submitButton).toBeInTheDocument();
        fireEvent.click(submitButton);


        await waitFor(() => expect(axiosMock.history.post.length).toBe(1));

        expect(axiosMock.history.post[0].params).toEqual(
            {
            "minMag": "5.8",
            "distance": "100",
        });

        expect(mockToast).toBeCalledWith("1 Earthquake Retrieved");
        expect(mockToast).toHaveBeenCalledTimes(1);
    });


    test("when you fill in the form and hit submit, it makes a request to the backend with two tests", async () => {
        const queryClient = new QueryClient();

        axiosMock.onPost("/api/earthquakes/retrieve").reply( 202, earthquakeFixtures.twoEarthquakeResponses );

        const { getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <EarthquakeCreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => {
            expect(getByTestId("EarthquakesForm-minMag")).toBeInTheDocument();
        });

        const minMagField = getByTestId("EarthquakesForm-minMag");
        const distField = getByTestId("EarthquakesForm-dist");
        const submitButton = getByTestId("EarthquakesForm-submit");


        fireEvent.change(minMagField, { target: { value: '2' } });
        fireEvent.change(distField, { target: { value: '100' } });

        expect(submitButton).toBeInTheDocument();
        fireEvent.click(submitButton);


        await waitFor(() => expect(axiosMock.history.post.length).toBe(1));

        expect(axiosMock.history.post[0].params).toEqual(
            {
            "minMag": "2",
            "distance": "100",
        });

        expect(mockToast).toBeCalledWith("2 Earthquakes Retrieved");
        expect(mockToast).toHaveBeenCalledTimes(1);
    });

});


