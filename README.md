<div align="center">
    <a style="text-decoration: none" href="https://travis-ci.org/liferay-mobile/apio-consumer-android">
        <img src="https://travis-ci.org/liferay-mobile/apio-consumer-android.svg?branch=master" alt="Travis CI" />
    </a>
    <a style="text-decoration: none"  href='https://www.gnu.org/licenses/lgpl-3.0'>
        <img src='https://img.shields.io/badge/License-LGPL%20v3-blue.svg' alt='License: LGPL v3' />
    </a>
</div>


# Apio Consumer for Android

**Apio Consumer for Android** is part of the [Apio project](#the-apio-project), which aims to promote the creation of APIs designed to evolve over time. The Apio Consumer is a client-side library to facilitate the creation of consumers of any hypermedia API.

The project serves as the consumer of APIs developed with [Apio Architect](https://github.com/liferay/com-liferay-apio-architect) and is heavily opinionated to reduce the amount of code API developers have to write. This is also achieved by out-of-the-box support for well-known patterns in hypermedia and REST APIs, such as the Collection Pattern.

It also has "smart" functionality, like the the ability to automatically create a local graph to facilitate the construction of offline support.

The consumer can control what the response includes (e.g., fields, embedded resources, etc.), and decide which hypermedia format best fits its needs (e.g., HAL, JSON-LD, etc.).

The main objective behind Apio Consumer is to consume APIs that follow REST principles and implement a hypermedia format without much effort.

## Why should I use it?

When consuming a Hypermedia API, you must consider things like link traversing, caching, JSON parsing and so on. Because of this, we built Apio Consumer as a library that facilitates consuming Hypermedia APIs that follow all the principles, leaving you to worry only about your internal logic.

Therefore, you can focus on creating beautiful APIs that stand the test of time and leave the act of consuming them for the Apio Consumer.

## How will it help me?

Apio Consumer helps you by providing the following:

- Default visualization for some popular [schema.org](http://schema.org) models like BlogPosting, Person or DigitalDocument. Apio Consumer will render those models with a default XML layout that can be customized at will.
- Support for JSON+LD, Apio Consumer can parse JSON documents in that format and return the model with all the information.
- Integration with [Liferay Screens](http://github.com/liferay/liferay-screens), that provides a Screenlet called *LoginScreenlet* that is able to render any supported models just by passing the URL of the resource.
- A representation of your data in a local graph that allows you to traverse the information away from a network connection.
- An easy mechanism for customization of the layouts called *scenarios*, that lets you pass another XML layout that applies when you are representing the resource inside a list (a *row* scenario), a detail view or the full resource.
- A straight way of consuming your hypermedia API :D

By using Apio Consumer you can represent your API in fewer minutes than it takes to eat a delicious pie!

## Can I try the Apio Consumer right away?

Absolutely! 

Just launch the example application by importing it into Android Studio (import  and check all the power of Apio Consumer against a sample API.

You can check out that the code that powers the demo application is just a *ThingScreenlet* with some customization.


## How do I start consuming APIs with it?

If you don't want to create your own API for now, you can use our test server.

Simply use your favorite REST-request client to make a GET request to:

    http://apio-apiosample.wedeploy.io

To use the API, you must specify an `accept` HTTP header. If you want to try a Hypermedia representation format, you can start with the following to order JSON-LD:

    accept: application/ld+json

If you want to develop an API from an example, we also got you covered! All you need is an OSGi container with JAX-RS.

To try all this quickly, you can use our [docker image](https://hub.docker.com/r/ahdezma/apio-whiteboard/). Simply run this on your terminal, specifying the folder in which module hot-deploy occurs:

    docker run -p 8080:8080 -v "/Users/YOUR_USER/deploy:/deploy" -d ahdezma/apio-whiteboard

As simple as that, you have a JAX-RS application with Apio Architect running in an OSGi container. You can access it by making a request to `http://localhost:8080`.

Change the *Apio Consumer* example project to query that URL in the *MainActivity* code and try it out :)

Now you're ready to start surfing the Hypermedia world!

## The Apio Project

The Apio project provides a set of guidelines and software to build evolvable APIs and consumers.

### [Apio Architect](https://github.com/liferay/com-liferay-apio-architect)

The best way of developing hypermedia powered APIs without having to worry about formats, writing tons of code or learning the best practices.

### [Apio Guidelines](https://evolvable-apis.org/)

An opinionated way to do RESTful APIs for *evolvability* and *discoverability*. 

### Other Apio Consumers

The Apio project also contains (or will) other client-side libraries to facilitate the development of consumers for Apio REST APIs (or any Hypermedia API).

- Apio Consumer for iOS (coming soon)
- Apio Consumer for JS (coming soon)

## Contributing

Liferay welcomes any and all contributions! 

Pull requests with contributions should be sent to this repository. Those pull requests will be discussed and reviewed by the Engineering team before including them in the product.

## Bug Reporting and Feature Requests

Did you find a bug? Please file an issue for it at [https://issues.liferay.com](https://issues.liferay.com) selecting *PUBLIC - Apio (Apio)* as the project and *Apio Consumer* as the component.

If you'd like to suggest a new feature for Liferay, visit the [Ideas Dashboard](https://dev.liferay.com/participate/ideas) to submit and track the progress of your idea!

## License

This library is free software ("Licensed Software"); you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; including but not limited to, the implied warranty of MERCHANTABILITY, NONINFRINGEMENT, or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
